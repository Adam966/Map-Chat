using MapChatServer.Data;
using MapChatServer.Dtos;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using Newtonsoft.Json.Linq;
using System;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using System.IdentityModel.Tokens.Jwt;
using MapChatServer.Helpers;

namespace MapChatServer.Controllers
{
    
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : Controller
    {
        
        private readonly IAuthRepository _repo;
        private readonly Microsoft.Extensions.Configuration.IConfiguration _config;
        private readonly ICipherService _cipherService;
        public AuthController(IAuthRepository repo, Microsoft.Extensions.Configuration.IConfiguration config, ICipherService cipherService) 
        {
            _repo = repo;
            _config = config;
            _cipherService = cipherService;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] JObject token)
        {
            
            if (token.ContainsKey("token"))
            {
                UserAuthDto uInfo = await _repo.Login(token["token"].ToString());

                if (!(uInfo.facebookToken == null))
                {
                    
                    var claims = new[]
                    {
                        new Claim(ClaimTypes.NameIdentifier, uInfo.id.ToString()),
                        new Claim(ClaimTypes.Name, uInfo.FacebookId),
                        new Claim(ClaimTypes.GivenName, uInfo.FirstName),
                        new Claim(ClaimTypes.WindowsUserClaim, "map_chat")
                    };
                    
                    var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config.GetSection("AppSettings:Token").Value));

                    var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha512Signature);

                    var tokenDescriptor = new SecurityTokenDescriptor
                    {
                        Subject = new ClaimsIdentity(claims),
                        Expires = DateTime.Now.AddDays(61),
                        SigningCredentials = creds
                    };
                    

                    var tokenHandler = new JwtSecurityTokenHandler();

                    var mToken = tokenHandler.CreateToken(tokenDescriptor);
                    

                    TokenDto tokenDto = new TokenDto
                    {
                        token = tokenHandler.WriteToken(mToken),
                        facebookToken = _cipherService.Encrypt(uInfo.facebookToken)
                    };

                    return Ok(tokenDto);
                }
                else 
                {
                    return Unauthorized(new { error = "Relog required" });
                }
            }
            else 
            {
                return StatusCode(400);
            }
            
        }
    }
}
