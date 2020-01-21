using MapChatServer.Dtos;
using MapChatServer.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace MapChatServer.Data
{
    public interface IUserRepository
    {
        Task<Userinfo> GetUser(int id);
        Task<bool> SaveAll();
        Task<bool> UpdateUserData(int userID, string facebookID, string facebookToken);
        Task<IEnumerable<UserBasicDto>> GetFriends(int userID);
        Task<bool> addUserFriend(int idU, int idF);
        Task<bool> deleteFriend(int idU, int idF);
        Task<IEnumerable<MessageUserDto>> getUsersChat(int idU, int idR);
        Task<IEnumerable<MessageGroupDto>> getGroupChat(int idU, int idE);
        Task<dynamic> getChatUsers(int idU);
    }
}
