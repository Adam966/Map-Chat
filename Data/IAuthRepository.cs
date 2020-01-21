using MapChatServer.Dtos;
using System.Threading.Tasks;

namespace MapChatServer.Data
{
    
    public interface IAuthRepository
    {
        Task<UserAuthDto> Login(string fbToken);
    }
    
}
