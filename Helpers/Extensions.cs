using Microsoft.AspNetCore.Http;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;

namespace MapChatServer.Helpers
{
    public static class HttpRequestExtension
    {
        public static string GetHeader(this HttpRequest request, string key)
        {
            return request.Headers.FirstOrDefault(x => x.Key == key).Value.FirstOrDefault();
        }
    }

    public static class JwtSecurityTokenExtension
    {
        public static string GetTokenItem(this JwtSecurityToken securityToken, string token, string key) 
        {
            //JwtSecurityToken jwtToken = new JwtSecurityTokenHandler().ReadJwtToken(token);
            JwtSecurityToken jwtToken = new JwtSecurityToken(token);
            return jwtToken.Claims.First(claim => claim.Type == key).Value;
        }
    }
}
