using MapChatServer.Dtos;
using MapChatServer.Models;
using Newtonsoft.Json.Linq;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;

namespace MapChatServer.Data
{
    
    public class FacebookReqs : IFacebookReqs
    {
        private readonly string appID;
        private readonly string appSecret;
        private readonly string appToken;
       
        private readonly IHttpClientFactory _clientFactory;
        private readonly IConfiguration _config;
        public FacebookReqs(IHttpClientFactory clientFactory, IConfiguration config) 
        {
            _clientFactory = clientFactory;
            _config = config;

            appID = _config.GetSection("Facebook:appID").Value;
            appSecret = _config.GetSection("Facebook:appSecret").Value;
            appToken = _config.GetSection("Facebook:appToken").Value;
        }

        public async Task<FacebookTokenDto> VerifyToken(string fbToken)
        {
            string filledLink = $"https://graph.facebook.com/debug_token?input_token=[USERTOKEN]&access_token={appToken}";
            filledLink = filledLink.Replace("[USERTOKEN]", fbToken);

            var request = new HttpRequestMessage(HttpMethod.Get, filledLink);

            var client = _clientFactory.CreateClient();
            var response = await client.SendAsync(request).ConfigureAwait(true);

            if (response.IsSuccessStatusCode)
            {
                string responseBody = await response.Content.ReadAsStringAsync().ConfigureAwait(true);
                JObject responseObj = JObject.Parse(responseBody);

                if (responseObj["data"]?["error"] == null)
                {
                    FacebookTokenDto ftDto = new FacebookTokenDto
                    {
                        appID = responseObj["data"]["app_id"].ToString(),
                        userID = responseObj["data"]["user_id"].ToString(),
                        isValid = responseObj["data"]["is_valid"].ToString().ToLower() == "true" ? true : false
                    };

                    return ftDto;
                }
                return new FacebookTokenDto(); 
            }
            else
            {
                return new FacebookTokenDto();
            }
        }

        public async Task<string> ExtendToken(string fbToken)
        {
            string filledLink =  $"https://graph.facebook.com/v5.0/oauth/access_token?grant_type=fb_exchange_token&client_id={appID}&client_secret={appSecret}&fb_exchange_token=[USERTOKEN]";
            filledLink = filledLink.Replace("[USERTOKEN]", fbToken);

            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, filledLink);

            HttpClient client = _clientFactory.CreateClient();
            HttpResponseMessage response = await client.SendAsync(request).ConfigureAwait(true);

            if (response.IsSuccessStatusCode)
            {
                string responseBody = await response.Content.ReadAsStringAsync().ConfigureAwait(true);
                JObject responseObj = JObject.Parse(responseBody);

                return responseObj["access_token"].ToString();
            }
            else
            {
                return null;
            }
        }

        public async Task<Userinfo> GetUserInfo(string facebookID, string userToken)
        {
            string apiLink = $"https://graph.facebook.com/{facebookID}/?fields=first_name,last_name&access_token={userToken}";
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, requestUri: apiLink);

            HttpClient client = _clientFactory.CreateClient();
            HttpResponseMessage response = await client.SendAsync(request).ConfigureAwait(true);

            if (response.IsSuccessStatusCode)
            {
                string responseBody = await response.Content.ReadAsStringAsync().ConfigureAwait(true);
                JObject responseObj = JObject.Parse(responseBody);

                if (responseObj["data"]?["error"] == null)
                {
                    Userinfo uInfo = new Userinfo
                    {
                        FirstName = responseObj["first_name"].ToString(),
                        LastName = responseObj["last_name"].ToString()
                    };
                    return uInfo;
                }
                return null;
            }
            else
            {
                return null;
            }
        }

        public async Task<string> GetUserPhoto(string facebookID, string userToken)
        {
            string apiLink = $"https://graph.facebook.com/{facebookID}/picture?type=large&redirect=false&width=400&height=400&access_token={userToken}";
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, apiLink);

            HttpClient client = _clientFactory.CreateClient();
            HttpResponseMessage response = await client.SendAsync(request).ConfigureAwait(true);

            if (response.IsSuccessStatusCode)
            {
                string responseBody = await response.Content.ReadAsStringAsync().ConfigureAwait(true);
                JObject responseObj = JObject.Parse(responseBody);

                return responseObj["data"]["url"].ToString();
            }
            else
            {
                return null;
            }
        }

        public async Task<List<string>> GetUserFriends(string facebookID, string userToken)
        {
            string apiLink = $"https://graph.facebook.com/{facebookID}/friends?access_token={userToken}";
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, apiLink);

            var client = _clientFactory.CreateClient();
            var response = await client.SendAsync(request).ConfigureAwait(true);

            if (response.IsSuccessStatusCode)
            {
                string responseBody = await response.Content.ReadAsStringAsync().ConfigureAwait(true);
                JObject responseObj = JObject.Parse(responseBody);

                dynamic obj = responseObj;
                List<string> userIDs = new List<string>();

                foreach (dynamic item in obj.data)
                {
                    userIDs.Add((string)item.id);
                }

                return userIDs;
            }
            else
            {
                return null;
            }
        }
    }
    
}
