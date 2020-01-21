using MapChatServer.Dtos;
using MapChatServer.Models;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;

namespace MapChatServer.Data
{
    
    public class AuthRepository : IAuthRepository
    {
        private readonly IFacebookReqs _facebookReqs;
        private readonly DataContext _context;
        public AuthRepository(IFacebookReqs facebookReqs, DataContext context)
        {
            _facebookReqs = facebookReqs;
            _context = context;
        }
        public async Task<UserAuthDto> Login(string fbToken)
        {
            FacebookTokenDto ftDto = await _facebookReqs.VerifyToken(fbToken).ConfigureAwait(true); //verifies token, returns token information
            UserAuthDto uAuth = new UserAuthDto();

            if (ftDto.isValid == true) 
            {
                ftDto.facebookToken = await _facebookReqs.ExtendToken(fbToken).ConfigureAwait(true); //extends token lifetime, returns extended token

                if (ftDto.facebookToken != null)
                {
                    //check if database contains given user, write if no
                    Userinfo user = await _context.Userinfo.FirstOrDefaultAsync(x => x.FacebookId == ftDto.userID).ConfigureAwait(true);

                    
                    if (user != null)
                    {
                        if (ftDto.appID == "810135372801052") //check if appid matches
                        {
                            uAuth.id = user.Id;
                            uAuth.FacebookId = user.FacebookId;
                            uAuth.FirstName = user.FirstName;
                            uAuth.LastName = user.LastName;
                            uAuth.facebookToken = ftDto.facebookToken;

                            return uAuth;
                        }
                        else { return uAuth; }
                    }
                    else 
                    {
                        Userinfo uInfo = await _facebookReqs.GetUserInfo(ftDto.userID, ftDto.facebookToken).ConfigureAwait(true);
                        string userProfilePhoto = await _facebookReqs.GetUserPhoto(ftDto.userID, ftDto.facebookToken).ConfigureAwait(true);

                        uInfo.FacebookId = ftDto.userID;
                        uInfo.ProfilePhotoUrl = userProfilePhoto;

                        await _context.Userinfo.AddAsync(uInfo);
                        await _context.SaveChangesAsync().ConfigureAwait(true);

                        uAuth.id = uInfo.Id;
                        uAuth.FacebookId = uInfo.FacebookId;
                        uAuth.FirstName = uInfo.FirstName;
                        uAuth.LastName = uInfo.LastName;
                        uAuth.facebookToken = ftDto.facebookToken;

                        return uAuth;
                    }
                }
                else { return uAuth; }
            }
            else 
            {
                return uAuth;
            }
        }
    }
    
}
