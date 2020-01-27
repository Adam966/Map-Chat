using AutoMapper;
using MapChatServer.Dtos;
using MapChatServer.Models;

namespace MapChatServer.Helpers
{
    public class AutoMapperProfiles : Profile
    {
        public AutoMapperProfiles() 
        {
            CreateMap<Userinfo, UserBasicDto>();

            CreateMap<EventDto, Eventgroup>();

            CreateMap<Location, Location>();

            CreateMap<Location, LocationDetailedDto>();

            CreateMap<Userinfo, UserBasicDto>();

            CreateMap<Eventgroup, EventGroupDetailedDto>()
                .ForMember(dest => dest.location, opt => opt.MapFrom(src => src.IdLNavigation));

            CreateMap<Messageu, MessageUserDto>();

            CreateMap<MessageUserDto, Messageu> ();

            CreateMap<Messageeg, MessageGroupDto>();

            CreateMap<MessageGroupDto, Messageeg>();
        }
    }
}
