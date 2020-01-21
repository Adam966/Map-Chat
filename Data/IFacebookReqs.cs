using MapChatServer.Dtos;
using MapChatServer.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace MapChatServer.Data
{
    public interface IFacebookReqs
    {
        Task<FacebookTokenDto> VerifyToken(string fbToken);

        Task<string> ExtendToken(string fbToken);

        Task<Userinfo> GetUserInfo(string facebookID, string userToken);

        Task<string> GetUserPhoto(string facebookID, string userToken);

        Task<List<string>> GetUserFriends(string facebookID, string userToken);
    }
}
