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
    public class MapChatRepository : IMapChatRepository
    {
        private readonly DataContext _context;
        private readonly IMapper _mapper;
        public MapChatRepository(DataContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        public async Task<bool> createEvent(EventDto userEvent)
        {
            Eventgroup isEventCreated = await _context.Eventgroup.Where(eg => eg.idU == userEvent.idU && eg.GroupName == userEvent.groupName).FirstOrDefaultAsync().ConfigureAwait(true);

            if (isEventCreated == null)
            {
                var executeTagInserts = new Action<Eventgroup>(async (Eventgroup userEventInsert) => {

                    string tagInsertion = "";
                    string simpleTags = "";
                    for (int i = 0; i < userEvent.tags.Length; i++)
                    {
                        tagInsertion += $"(\"{userEvent.tags[i]}\")";
                        simpleTags += $"\"{userEvent.tags[i]}\"";
                        if (i < userEvent.tags.Length - 1)
                        {
                            tagInsertion += ",";
                            simpleTags += ",";
                        }
                    }

                    string bal = $"insert ignore into tag(tagText) values{tagInsertion};";

                    await _context.Database.ExecuteSqlRawAsync($"insert ignore into tag(tagText) values{tagInsertion};").ConfigureAwait(true);

                    List<Tag> tags = await _context.Tag.FromSqlRaw($"select * from tag where tagText in({simpleTags});").ToListAsync().ConfigureAwait(true);

                    string groupTagInsertion = "";
                    for (int i = 0; i < tags.Count; i++)
                    {
                        groupTagInsertion += $"({userEventInsert.Id},{tags[i].Id})";
                        if (i < tags.Count - 1)
                        {
                            groupTagInsertion += ",";
                        }
                    }
                    await _context.Database.ExecuteSqlRawAsync($"insert into eventtag(idEG, idT) values{groupTagInsertion};").ConfigureAwait(true);
                });

                Eventgroup userEventInsert = _mapper.Map<Eventgroup>(userEvent);
                Location eventLocation = _mapper.Map<Location>(userEvent.location);

                await _context.Location.AddAsync(eventLocation);
                await _context.SaveChangesAsync().ConfigureAwait(true);

                userEventInsert.IdL = eventLocation.Id;

                await _context.Eventgroup.AddAsync(userEventInsert);
                await _context.SaveChangesAsync().ConfigureAwait(true);

                Eventgroupuser eGroupUser = new Eventgroupuser
                {
                    IdEg = userEventInsert.Id,
                    IdU = userEventInsert.idU,
                    Admin = 1
                };

                await _context.Eventgroupuser.AddAsync(eGroupUser);
                await _context.SaveChangesAsync().ConfigureAwait(true);

                if (userEvent.tags != null)
                {
                    executeTagInserts(userEventInsert);
                }

                return true;
            }
            else { return false; }
        }

        public async Task<bool> deleteEvent(int userID, int eventID)
        {
            Eventgroup userEvent = await _context.Eventgroup.Where(eg => eg.idU == userID && eg.Id == eventID).FirstOrDefaultAsync().ConfigureAwait(true);

            if (userEvent != null)
            {
                userEvent.active = 0;
                _context.Eventgroup.Update(userEvent);
                await _context.SaveChangesAsync().ConfigureAwait(true);
                return true;
            }
            else
            {
                return false;
            }
        }

        public async Task<IEnumerable<EventGroupDetailedDto>> getUserEvents(int userID)
        {
            IEnumerable<Eventgroup> eGroup = await _context.Eventgroup.Where(u => u.idU == userID && u.active == 1)
                .Include(l => l.IdLNavigation)
                .ToListAsync().ConfigureAwait(true);

            IEnumerable<EventGroupDetailedDto> eGroupMapped = _mapper.Map<IEnumerable<EventGroupDetailedDto>>(eGroup);

            return eGroupMapped;
        }
        public async Task<IEnumerable<string>> getEventTags(int eventID)
        {
            Eventgroup eGroup = await _context.Eventgroup.Where(u => u.Id == eventID).FirstOrDefaultAsync().ConfigureAwait(true);

            if (eGroup != null)
            {
                IEnumerable<string> eTag = await _context.Tag.Where(x => _context.Eventtag.Any(c => c.IdT == x.Id && c.IdEg == eventID))
                                          .Select(t => t.TagText).ToListAsync().ConfigureAwait(true);
                return eTag;
            }
            else
            {
                return null;
            }
        }

        public async Task<IEnumerable<UserBasicDto>> getEventUsers(int eventID)
        {
            
            IEnumerable<Userinfo> eventUsers = await _context.Userinfo.Where(x => _context.Eventgroupuser
                        .Any(c => c.IdU == x.Id && c.IdEg == eventID && c.Active == 1)).ToListAsync().ConfigureAwait(true);
                        

            IEnumerable<UserBasicDto> eGroupMapped = _mapper.Map<IEnumerable<UserBasicDto>>(eventUsers);

            
            return eGroupMapped;
        }

        public async Task<IEnumerable<EventGroupDetailedDto>> getEvents()
        {
            IEnumerable<Eventgroup> eGroup = await _context.Eventgroup.Where(u => u.active == 1)
                .Include(l => l.IdLNavigation)
                .ToListAsync().ConfigureAwait(true);

            IEnumerable<EventGroupDetailedDto> eGroupMapped = _mapper.Map<IEnumerable<EventGroupDetailedDto>>(eGroup);

            return eGroupMapped;
        }

        public async Task<bool> leaveEvent(int idU, int idE)
        {
            Eventgroupuser eUser = await _context.Eventgroupuser.Where(u => u.IdEg == idE && u.IdU == idU).FirstOrDefaultAsync().ConfigureAwait(true);

            if (eUser != null)
            {
                eUser.Active = 0;
                await _context.SaveChangesAsync().ConfigureAwait(true);

                return true;
            }
            return false;
        }

        public async Task<bool> removeUserFromEvent(int idU, int idUR, int idE)
        {
            Eventgroupuser eUser = await _context.Eventgroupuser
                .Where(u => u.IdEg == idE && u.IdU == idUR && u.Admin == 0).FirstOrDefaultAsync().ConfigureAwait(true);

            if (eUser != null)
            {
                Eventgroup eG = await _context.Eventgroup.Where(eg => eg.idU == idUR && eg.Id == idE).FirstOrDefaultAsync().ConfigureAwait(true);

                Eventgroupuser eUserAdm = await _context.Eventgroupuser
                .Where(u => u.IdEg == idE && u.IdU == idU && u.Admin == 1).FirstOrDefaultAsync().ConfigureAwait(true);

                if (eG == null && eUserAdm != null)
                {
                    eUser.Active = 0;
                    await _context.SaveChangesAsync().ConfigureAwait(true);

                    return true;
                }
                return false;
            }
            return false;
        }

        public async Task<bool> revokeAdmin(int idU, int idUR, int idE)
        {
            Eventgroupuser eUser = await _context.Eventgroupuser
                .Where(u => u.IdEg == idE && u.IdU == idUR && u.Admin == 1).FirstOrDefaultAsync().ConfigureAwait(true);

            if (eUser != null)
            {
                Eventgroup eG = await _context.Eventgroup.Where(eg => eg.idU == idU && eg.Id == idE).FirstOrDefaultAsync().ConfigureAwait(true);

                if (eG != null)
                {
                    eUser.Admin = 0;
                    await _context.SaveChangesAsync().ConfigureAwait(true);

                    return true;
                }
                return false;
            }
            return false;
        }

        public async Task<bool> addAdmin(int idU, int idUR, int idE)
        {
            Eventgroupuser eUser = await _context.Eventgroupuser
                .Where(u => u.IdEg == idE && u.IdU == idUR && u.Admin == 0).FirstOrDefaultAsync().ConfigureAwait(true);

            if (eUser != null) 
            {
                Eventgroup eG = await _context.Eventgroup.Where(eg => eg.idU == idU && eg.Id == idE).FirstOrDefaultAsync().ConfigureAwait(true);

                if (eG != null)
                {
                    eUser.Admin = 1;
                    await _context.SaveChangesAsync().ConfigureAwait(true);

                    return true;
                }
                return false;
            }
            return false;
        }

        public async Task<bool> joinEvent(int idU, int idE)
        {
            Eventgroup eGroup = await _context.Eventgroup.Where(eg => eg.Type == 0 && eg.Id == idE).FirstOrDefaultAsync().ConfigureAwait(true);

            Eventgroupuser eUser = await _context.Eventgroupuser
                .Where(u => u.IdEg == idE && u.IdU == idU).FirstOrDefaultAsync().ConfigureAwait(true);

            if (eGroup != null && eUser == null)
            {
                Eventgroupuser eGUser = new Eventgroupuser
                {
                    IdU = idU,
                    IdEg = idE,
                    Admin = 0
                };

                await _context.AddAsync(eGUser).ConfigureAwait(true);
                await _context.SaveChangesAsync().ConfigureAwait(true);

                return true;
            }
            return false;
        }

        public async Task<bool> addUserToGroup(int idU, int idUTA, int idE)
        {
            Eventgroupuser eUser = await _context.Eventgroupuser
                .Where(u => u.IdEg == idE && u.IdU == idU).FirstOrDefaultAsync().ConfigureAwait(true);

            if (eUser != null) 
            {
                Eventgroupuser eUserToAdd = await _context.Eventgroupuser
                    .Where(u => u.IdEg == idE && u.IdU == idUTA).FirstOrDefaultAsync().ConfigureAwait(true);

                if (eUserToAdd == null) 
                {
                    Eventgroupuser eGUser = new Eventgroupuser
                    {
                        IdU = idUTA,
                        IdEg = idE,
                        Admin = 0
                    };

                    await _context.AddAsync(eGUser).ConfigureAwait(true);
                    await _context.SaveChangesAsync().ConfigureAwait(true);

                    return true;
                }
                return false;
            }
            return false;
        }

        public async Task<EventGroupDetailedDto> getEventByID(int idU, int idE)
        {
            Eventgroup eGroup = await _context.Eventgroup.Where(x => x.Id == idE)
                .Include(l => l.IdLNavigation)
                .FirstOrDefaultAsync().ConfigureAwait(true);

            EventGroupDetailedDto eGroupMapped = _mapper.Map<EventGroupDetailedDto>(eGroup);
            return eGroupMapped;
        }
    }
}
