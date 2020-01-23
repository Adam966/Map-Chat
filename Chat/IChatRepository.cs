using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Chat
{
    public interface IChatRepository
    {
        Task writeUserMessagePrivate(string senderID, string receiverID, string messageText);
    }
}
