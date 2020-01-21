using AutoMapper;
using MapChatServer.Data;
using MapChatServer.Dtos;
using MapChatServer.Helpers;
using MapChatServer.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Controllers
{
    [ServiceFilter(typeof(LogUserActivity))]
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly ICipherService _cipherService;
        private readonly IUserRepository _userRepository;
        private readonly IMapper _mapper;
        public UserController(ICipherService cipherService, IUserRepository userRepository, IMapper mapper)
        {
            _cipherService = cipherService;
            _userRepository = userRepository;
            _mapper = mapper;
        }

        [HttpGet("userInfo")]
        public async Task<IActionResult> GetUserInfo()
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", string.Empty);

            string userId = new JwtSecurityToken().GetTokenItem(authorization, "nameid");

            Userinfo uinfo = await _userRepository.GetUser(int.Parse(userId)).ConfigureAwait(true);

            UserBasicDto userToReturn = _mapper.Map<UserBasicDto>(uinfo);

            return Ok(userToReturn);
        }

        [HttpGet("friends")]
        public async Task<IActionResult> GetUserFriends()
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", string.Empty);

            string userId = new JwtSecurityToken().GetTokenItem(authorization, "nameid");

            IEnumerable<UserBasicDto> userFriends = await _userRepository.GetFriends(int.Parse(userId)).ConfigureAwait(true);

            return Ok(userFriends);
        }

        [HttpGet("updateUserData")]
        public async Task<IActionResult> UpdateUserData()
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", string.Empty);

            string fToken = Request.GetHeader("fToken");

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));
            string userFacebookId = new JwtSecurityToken().GetTokenItem(authorization, "unique_name");
            string userFacebookToken = _cipherService.Decrypt(fToken);

            bool isSuccess = await _userRepository.UpdateUserData(userId, userFacebookId, userFacebookToken).ConfigureAwait(true);

            if (isSuccess) { return Ok(); }
            else { return NotFound(new { error = "Relog or privilege required" }); }
        }

        [HttpGet("addFriend")]
        public async Task<IActionResult> addFriend(int idF)
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            bool isSuccess = await _userRepository.addUserFriend(userId, idF).ConfigureAwait(true);

            if (isSuccess) { return Ok(); } else { return BadRequest(); }
        }

        [HttpGet("deleteFriend")]
        public async Task<IActionResult> deleteFriend(int idF) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            bool isSuccess = await _userRepository.deleteFriend(userId, idF).ConfigureAwait(true);

            if (isSuccess) { return Ok(); } else { return BadRequest(); }
        }

        [HttpGet("getUsersChat")]
        public async Task<IActionResult> getUsersChat(int idR) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            IEnumerable<MessageUserDto> messages = await _userRepository.getUsersChat(userId, idR).ConfigureAwait(true);

            return Ok(messages);
        }

        [HttpGet("getGroupChat")]
        public async Task<IActionResult> getGroupChat(int idE) 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            IEnumerable<MessageGroupDto> messages = await _userRepository.getGroupChat(userId, idE).ConfigureAwait(true);

            if (messages != null) { return Ok(messages); } else { return BadRequest(); }
        }

        [HttpGet("getChatUsers")]
        public async Task<IActionResult> getChatUsers() 
        {
            string authorization = Request.GetHeader("Authorization");
            authorization = authorization.Replace("Bearer ", newValue: string.Empty);

            int userId = int.Parse(new JwtSecurityToken().GetTokenItem(authorization, "nameid"));

            dynamic users = await _userRepository.getChatUsers(userId).ConfigureAwait(true);

            return Ok(users);
        }
    }
}
