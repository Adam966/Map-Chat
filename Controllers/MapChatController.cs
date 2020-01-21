using AutoMapper;
using MapChatServer.Data;
using MapChatServer.Dtos;
using MapChatServer.Helpers;
using MapChatServer.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Threading.Tasks;

namespace MapChatServer.Controllers
{
    [ServiceFilter(typeof(LogUserActivity))]
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class MapChatController : ControllerBase
    {
        private readonly ICipherService _cipherService;
        private readonly IMapChatRepository _mapChatRepository;
        private readonly IMapper _mapper;
        public MapChatController(ICipherService cipherService, IMapChatRepository mapChatRepository, IMapper mapper) 
        {
            _cipherService = cipherService;
            _mapChatRepository = mapChatRepository;
            _mapper = mapper;
        }

        [HttpPost("createEvent")]
        public async Task<IActionResult> CreateEvent([FromBody] EventDto userEvent) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));
            
            userEvent.idU = userId;

            bool isSucceed = await _mapChatRepository.createEvent(userEvent).ConfigureAwait(true);

            if (isSucceed)
            {
                return StatusCode(201);
            }
            else 
            {
                return StatusCode(304);
            }
        }

        [HttpGet("deleteEvent")]
        public async Task<IActionResult> DeleteEvent(int idE) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            bool isSucceed = await _mapChatRepository.deleteEvent(userId, idE).ConfigureAwait(true);

            if (isSucceed)
            {
                return Ok();
            }
            else
            {
                return NotFound(new { error="group does not exist"});
            }
        }

        [HttpGet("getUserEvents")]
        public async Task<IActionResult> getUserEvents() 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            IEnumerable<EventGroupDetailedDto> eGroup = await _mapChatRepository.getUserEvents(userId).ConfigureAwait(true);

            return Ok(eGroup);
        }

        [HttpGet("getEventTags")]
        public async Task<IActionResult> getEventTags(int idE) 
        {
            IEnumerable<string> eTags = await _mapChatRepository.getEventTags(idE).ConfigureAwait(true);

            return Ok(new { tags = eTags});
        }

        [HttpGet("getEventUsers")]
        public async Task<IActionResult> getEventUsers(int idE) 
        {
            IEnumerable<UserBasicDto> eventUsers = await _mapChatRepository.getEventUsers(idE).ConfigureAwait(true);

            return Ok(eventUsers);
        }

        [HttpGet("getEvents")]
        public async Task<IActionResult> getEvents()
        {
            IEnumerable<EventGroupDetailedDto> eventUsers = await _mapChatRepository.getEvents().ConfigureAwait(true);

            return Ok(eventUsers);
        }

        [HttpGet("leaveEvent")]
        public async Task<IActionResult> leaveEvent(int idE) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            bool isSuccess = await _mapChatRepository.leaveEvent(userId, idE).ConfigureAwait(true);

            if (isSuccess) { return Ok(); } else { return BadRequest(); }
        }

        [HttpGet("removeUserFromEvent")]
        public async Task<IActionResult> removeUFromEvent(int idUR, int idE) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            bool isSuccess = await _mapChatRepository.removeUserFromEvent(userId, idUR, idE).ConfigureAwait(true);

            if (isSuccess) { return Ok(); } else { return BadRequest(); }
        }

        [HttpGet("revokeAdmin")]
        public async Task<IActionResult> revokeAdmin(int idUR, int idE) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            bool isSuccess = await _mapChatRepository.revokeAdmin(userId, idUR, idE).ConfigureAwait(true);

            if (isSuccess) { return Ok(); } else { return BadRequest(); }
        }

        [HttpGet("addAdminPrivilege")]
        public async Task<IActionResult> addAdmin(int idUR, int idE) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            bool isSuccess = await _mapChatRepository.addAdmin(userId, idUR, idE).ConfigureAwait(true);

            if (isSuccess) { return Ok(); } else { return BadRequest(); }
        }

        [HttpGet("joinEvent")]
        public async Task<IActionResult> joinEvent(int idE)
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            bool isSuccess = await _mapChatRepository.joinEvent(userId, idE).ConfigureAwait(true);

            if (isSuccess) { return Ok(); } else { return BadRequest(); }
        }

        [HttpGet("addUserToGroup")]
        public async Task<IActionResult> addUserToGroup(int idUTA, int idE) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            bool isSuccess = await _mapChatRepository.addUserToGroup(userId, idUTA, idE).ConfigureAwait(true);

            if (isSuccess) { return Ok(); } else { return BadRequest(); }
        }

        [HttpGet("getEventByID")]
        public async Task<IActionResult> getEventByID(int idE) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            EventGroupDetailedDto myEvent = await _mapChatRepository.getEventByID(userId, idE).ConfigureAwait(true);

            return Ok(myEvent);
        }
    }
}
