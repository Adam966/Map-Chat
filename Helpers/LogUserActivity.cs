using MapChatServer.Data;
using Microsoft.AspNetCore.Mvc.Filters;
using System;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;

namespace MapChatServer.Helpers
{
    
    public class LogUserActivity : IAsyncActionFilter
    {
        public async Task OnActionExecutionAsync(ActionExecutingContext context, ActionExecutionDelegate next)
        {
            ActionExecutedContext resultContext = await next().ConfigureAwait(true);

            var userId = int.Parse(resultContext.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value);

            var repo = resultContext.HttpContext.RequestServices.GetService<IUserRepository>();

            var user = await repo.GetUser(userId).ConfigureAwait(true);
            if (user != null) 
            {
                user.LastOnline = DateTime.Now;
                await repo.SaveAll().ConfigureAwait(true);
            }
        }
    }
    
}
