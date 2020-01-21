using MapChatServer.Dtos;
using MapChatServer.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace MapChatServer.Data
{
    public interface IMapChatRepository
    {
        Task<bool> createEvent(EventDto userEvent);
        Task<bool> deleteEvent(int userID, int eventID);
        Task<IEnumerable<EventGroupDetailedDto>> getUserEvents(int userID);
        Task<IEnumerable<string>> getEventTags(int eventID);
        Task<IEnumerable<UserBasicDto>> getEventUsers(int eventID);
        Task<IEnumerable<EventGroupDetailedDto>> getEvents();
        Task<bool> leaveEvent(int idU, int idE);
        Task<bool> removeUserFromEvent(int idU, int idUR, int idE);
        Task<bool> revokeAdmin(int idU, int idUR, int idE);
        Task<bool> addAdmin(int idU, int idUR, int idE);
        Task<bool> joinEvent(int idU, int idE);
        Task<bool> addUserToGroup(int idU, int idUTA, int idE);
        Task<EventGroupDetailedDto> getEventByID(int idU, int idE);
    }
}
