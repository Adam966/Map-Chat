using MapChatServer.Dtos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Chat
{
    public interface IChatRepository
    {
        Task writeUserMessagePrivate(MessageUserDto messageDto);
        Task<bool> checkUserPrivilege(int userID, int groupID);
        Task writeUserMessageGroup(MessageGroupDto messageDto);
    }
}
