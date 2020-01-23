using MapChatServer.Chat;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.SignalR;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Hubs
{
    [Authorize]
    public class ChatHub : Hub
    {
        private static List<IdentifierO> connections = new List<IdentifierO>();
        private readonly IChatRepository _chatRepository;

        public ChatHub(IChatRepository chatRepository) 
        {
            _chatRepository = chatRepository;
        }
        public override Task OnConnectedAsync() 
        {
            
            string connID = Context.ConnectionId;
            string uID = Context.UserIdentifier;
            
            IdentifierO firstMatch = connections.Where(id => id.userID == uID).FirstOrDefault();

            if (firstMatch == null) 
            {
                connections.Add(new IdentifierO { connectionID = connID, userID = uID });
            }
            Debug.Write("CONNECTED: " + connID + " NAME: " + uID);

            return base.OnConnectedAsync(); 
        }

        public override Task OnDisconnectedAsync(Exception exception)
        {
            string connID = Context.ConnectionId;

            IdentifierO firstMatch = connections.Where(id => id.connectionID == connID).FirstOrDefault();

            if (firstMatch != null)
            {
                connections.Remove(firstMatch);
            }

            Debug.Write("DISCONNECTED: " + connID + " NAME: " + Context.UserIdentifier);

            return base.OnDisconnectedAsync(exception);
        }

        public async Task SendMessageUser(string userID, string message)
        {
            IdentifierO userConnection = connections.Where(id => id.userID == userID).FirstOrDefault();

            if (userConnection != null) 
            {
                Debug.Write("SendingMessageTo: " + userConnection.connectionID + " NAME: " + userConnection.userID);

                await Clients.Client(userConnection.connectionID).SendAsync("ReceiveMessagePrivate", message).ConfigureAwait(true);
            }
            Debug.Write("NAME: "+userID);

            await _chatRepository.writeUserMessagePrivate(Context.UserIdentifier, userID, message).ConfigureAwait(true);
        }
    }
}
