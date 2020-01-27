using MapChatServer.Chat;
using MapChatServer.Dtos;
using Microsoft.AspNetCore.SignalR;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Hubs
{
    public class GroupChatHub : Hub
    {
        private readonly IChatRepository _chatRepository;
        public GroupChatHub(IChatRepository chatRepository) 
        {
            _chatRepository = chatRepository;
        }

        public async Task AddToUGroup(int groupName)
        {
            string connID = Context.ConnectionId;

            bool result = await _chatRepository.checkUserPrivilege(int.Parse(Context.UserIdentifier), groupName).ConfigureAwait(true);

            if (result)
            {
                await Groups.AddToGroupAsync(connID, groupName.ToString()).ConfigureAwait(true);


                await Clients.Client(connID).SendAsync("groupConnection", new { idG = groupName, connected = true }).ConfigureAwait(true);

                Debug.Write("CONNECTED: " + Context.UserIdentifier + " NAME: " + groupName);
            }
            else
            {
                await Clients.Client(connID).SendAsync("groupConnection", new { idG = groupName, connected = false }).ConfigureAwait(true);

                Debug.Write("FAILED TO CONNECT: " + Context.UserIdentifier + " NAME: " + groupName);
            }
        }

        public async Task SendMessageGroup(MessageGroupDto message) 
        {
            message.IdU = int.Parse(Context.UserIdentifier);

            await Clients.Group(message.IdEg.ToString()).SendAsync("ReceiveMessageGroup", message).ConfigureAwait(true);

            await _chatRepository.writeUserMessageGroup(message).ConfigureAwait(true);

            Debug.Write("SENDING MESSAGE TO: " + message.IdEg + " NAME: " + message.IdU);
        }

    }
}
