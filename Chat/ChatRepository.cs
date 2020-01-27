using AutoMapper;
using MapChatServer.Dtos;
using MapChatServer.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Chat
{
    public class ChatRepository : IChatRepository
    {
        private readonly DataContext _context;
        private readonly IMapper _mapper;
        public ChatRepository(DataContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        public async Task writeUserMessagePrivate(MessageUserDto messagedto)
        {
            Messageu messageMapped = _mapper.Map<Messageu>(messagedto);

            await _context.Messageu.AddAsync(messageMapped).ConfigureAwait(true);
            await _context.SaveChangesAsync().ConfigureAwait(true);
        }

        public async Task<bool> checkUserPrivilege(int userID, int groupID)
        {
            Eventgroupuser eUser = await _context.Eventgroupuser.Where(x => x.IdU == userID && x.IdEg == groupID && x.Active == 1).FirstOrDefaultAsync().ConfigureAwait(true);

            if (eUser != null) { return true; }
            return false;
        }

        public async Task writeUserMessageGroup(MessageGroupDto messagedto)
        {
            Messageeg messageMapped = _mapper.Map<Messageeg>(messagedto);

            await _context.Messageeg.AddAsync(messageMapped).ConfigureAwait(true);
            await _context.SaveChangesAsync().ConfigureAwait(true);
        }
    }
}
