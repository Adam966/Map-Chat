using MapChatServer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Chat
{
    public class ChatRepository : IChatRepository
    {
        private readonly DataContext _context;
        public ChatRepository(DataContext context)
        {
            _context = context;
        }

        public async Task writeUserMessagePrivate(string senderID, string receiverID, string messageText)
        {
            Messageu message = new Messageu
            {
                SenderId = int.Parse(senderID),
                ReceiverId = int.Parse(receiverID),
                MessageText = messageText
            };

            await _context.Messageu.AddAsync(message).ConfigureAwait(true);
            await _context.SaveChangesAsync().ConfigureAwait(true);
        }
    }
}
