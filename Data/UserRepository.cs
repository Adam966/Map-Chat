using AutoMapper;
using MapChatServer.Dtos;
using MapChatServer.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Data
{
    public class UserRepository : IUserRepository
    {
        private readonly DataContext _context;
        private readonly IFacebookReqs _facebookReqs;
        private readonly IMapper _mapper;
        public UserRepository(DataContext context, IFacebookReqs facebookReqs, IMapper mapper)
        {
            _context = context;
            _facebookReqs = facebookReqs;
            _mapper = mapper;
        }

        public async Task<Userinfo> GetUser(int id)
        {
            Userinfo user = await _context.Userinfo.FirstOrDefaultAsync(u => u.Id == id).ConfigureAwait(true);

            return user;
        }
        public async Task<bool> SaveAll()
        {
            return await _context.SaveChangesAsync().ConfigureAwait(true) > 0;
        }

        public async Task<bool> UpdateUserData(int userID, string facebookID, string facebookToken)
        {
            List<string> userFriendIds = await _facebookReqs.GetUserFriends(facebookID, facebookToken).ConfigureAwait(true);

            if (userFriendIds != null && userFriendIds.Count > 0)
            {
                dynamic userIds = await _context.Userinfo.Where(u => userFriendIds.Contains(u.FacebookId))
                    .Select(c => new
                    {
                        id = c.Id
                    })
                    .ToListAsync().ConfigureAwait(true);

                foreach (dynamic uid in userIds)
                {
                    await _context.Database.ExecuteSqlRawAsync($"insert ignore friend(userID, friendID) values({userID}, {(int)uid.id})").ConfigureAwait(true);
                }

                Userinfo uInfo = await _facebookReqs.GetUserInfo(facebookID, facebookToken).ConfigureAwait(true);
                string userProfilePhoto = await _facebookReqs.GetUserPhoto(facebookID, facebookToken).ConfigureAwait(true);

                uInfo.Id = userID;
                uInfo.FacebookId = facebookID;
                uInfo.ProfilePhotoUrl = userProfilePhoto;

                _context.Userinfo.Update(uInfo);
                await _context.SaveChangesAsync().ConfigureAwait(true);

                return true;
            }
            else
            {
                return false;
            }
        }
        public async Task<IEnumerable<UserBasicDto>> GetFriends(int userID)
        {
            dynamic friendIds = await _context.Friend.Where(f => f.UserId == userID && f.Active == 1)
                .Select(c => new
                {
                    id = c.FriendId
                }).ToListAsync().ConfigureAwait(true);
            List<int> friendIdList = new List<int>();
            foreach (dynamic di in friendIds)
            {
                friendIdList.Add((int)di.id);
            }
            IEnumerable<Userinfo> friends = await _context.Userinfo.Where(r => friendIdList.Contains(r.Id)).ToListAsync().ConfigureAwait(true);

            IEnumerable<UserBasicDto> mappedfriends = _mapper.Map<IEnumerable<UserBasicDto>>(friends);

            return mappedfriends;
        }
        public async Task<bool> addUserFriend(int idU, int idF)
        {
            Userinfo uFriend = await _context.Userinfo.Where(u => u.Id == idF).FirstOrDefaultAsync().ConfigureAwait(true);

            if (uFriend != null)
            {
                await _context.Database.ExecuteSqlRawAsync($"insert into Friend(userID, friendID) values({idU}, {idF}), ({idF}, {idU}) on duplicate key update active = 1;").ConfigureAwait(true);

                return true;
            }
            return false;
        }

        public async Task<bool> deleteFriend(int idU, int idF)
        {
            Friend uFriend = await _context.Friend.Where(u => u.UserId == idF).FirstOrDefaultAsync().ConfigureAwait(true);

            if (uFriend != null) 
            {
                await _context.Database.ExecuteSqlRawAsync($"update Friend set active = 0 where ((userID like {idU} and friendID like {idF}) or (userID like {idF} and friendID like {idU}));").ConfigureAwait(true);

                return true;
            }
            return false;
        }

        public async Task<IEnumerable<MessageUserDto>> getUsersChat(int idU, int idR)
        {
            IEnumerable<Messageu> messages = await _context.Messageu.Where(s => s.SenderId == idU && s.ReceiverId == idR || s.SenderId == idR && s.ReceiverId == idU)
                .OrderBy(o => o.CTime).ToListAsync().ConfigureAwait(true);

            IEnumerable<MessageUserDto> mappedMessages = _mapper.Map<IEnumerable<MessageUserDto>>(messages);

            return mappedMessages;
        }

        public async Task<IEnumerable<MessageGroupDto>> getGroupChat(int idU, int idE)
        {
            Eventgroupuser egroupU = await _context.Eventgroupuser.Where(egu => egu.IdU == idU).FirstOrDefaultAsync().ConfigureAwait(true);

            if (egroupU != null) 
            {
                IEnumerable<Messageeg> messages = await _context.Messageeg.Where(g => g.IdEg == idE)
                    .OrderBy(o => o.CTime).ToListAsync().ConfigureAwait(true);

                IEnumerable<MessageGroupDto> mappedMessages = _mapper.Map<IEnumerable<MessageGroupDto>>(messages);

                return mappedMessages;
            }
            return null;
        }

        public Task<dynamic> getChatUsers(int idU)
        {
            throw new NotImplementedException();
            /*
            //var chatUsers = await _context.Messageu.Where(x => x.SenderId == idU || x.ReceiverId == idU).OrderBy(z => z.SenderId).ToListAsync().ConfigureAwait(true);

            var chatUsers = await _context.Messageu.Select(x => new {x.SenderId, x.ReceiverId }).Where(b => b.ReceiverId == idU || b.SenderId == idU).Distinct().ToListAsync().ConfigureAwait(true);

            var chatUsers2 = await _context.Messageu.FromSqlRaw($"").ToListAsync().ConfigureAwait(true);
            
            var chatUsers = await _context.Messageu
            .FromSqlRaw($"(select * from MessageU " +
            $"inner join Userinfo on Userinfo.id = MessageU.receiverID where MessageU.senderID = {idU} group by Messageu.senderID) " +
            $"UNION " +
            $"(select * from MessageU " +
            $"inner join Userinfo on Userinfo.id = MessageU.senderID where MessageU.receiverID  = {idU} group by Messageu.senderID);").ToListAsync().ConfigureAwait(true);
            
            return chatUsers;
            */
        }
    }
}
