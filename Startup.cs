using System;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using AutoMapper;
using MapChatServer.Helpers;
using MapChatServer.Data;
using MapChatServer.Models;
using Microsoft.AspNetCore.DataProtection;
using System.IO;
using Microsoft.AspNetCore.DataProtection.AuthenticatedEncryption.ConfigurationModel;
using Microsoft.AspNetCore.DataProtection.AuthenticatedEncryption;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using MapChatServer.Hubs;
using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;
using MapChatServer.Chat;

namespace MapChatServer
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {

            services.AddControllers();

            services.AddDbContextPool<DataContext>(options => options
              .UseMySql(Configuration.GetConnectionString("DefaultConnection")));
                  

            services.AddControllers().AddNewtonsoftJson(opt => {
                opt.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore;
            });

            services.AddAutoMapper(typeof(AutoMapperProfiles).Assembly);

            services.AddCors();

            services.AddHttpClient();

            services.AddScoped<IAuthRepository, AuthRepository>();

            services.AddScoped<IMapChatRepository, MapChatRepository>();

            services.AddScoped<IUserRepository, UserRepository>();

            services.AddScoped<LogUserActivity>();

            services.AddSingleton<IFacebookReqs, FacebookReqs>();

            services.AddSingleton<ICipherService, CipherService>();

            services.AddSignalR().AddHubOptions<ChatHub>(options =>
            {
                options.EnableDetailedErrors = true;
            });

            services.AddScoped<IChatRepository, ChatRepository>();


            services.AddDataProtection()
                .UseCryptographicAlgorithms(
                    new AuthenticatedEncryptorConfiguration()
                    {
                        EncryptionAlgorithm = EncryptionAlgorithm.AES_256_CBC,
                        ValidationAlgorithm = ValidationAlgorithm.HMACSHA256
                    })
                .PersistKeysToFileSystem(new DirectoryInfo("keys"))
                    .SetDefaultKeyLifetime(TimeSpan.FromDays(61));

            services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
                .AddJwtBearer(options =>
                {
                    options.TokenValidationParameters = new TokenValidationParameters
                    {
                        ValidateIssuerSigningKey = true,
                        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8
                        .GetBytes(Configuration.GetSection("AppSettings:Token").Value)),
                        ValidateIssuer = false,
                        ValidateAudience = false
                    };
                    
                    options.Events = new JwtBearerEvents
                    {

                        OnMessageReceived = context =>
                        {
                            var accessToken = context.Request.Query["access_token"];
                            // If the request is for our hub...
                            var path = context.HttpContext.Request.Path;

                            if (!string.IsNullOrEmpty(accessToken) && ((path.StartsWithSegments("/chat")) || (path.StartsWithSegments("/groupchat"))))
                            {
                                // Read the token out of the query string
                                context.Token = accessToken;
                            }
                            return Task.CompletedTask;
                        }
                    };
                    
                });

        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            app.UseDeveloperExceptionPage();
            //app.UseHttpsRedirection();
            //app.UseHsts();

            app.UseRouting();

            app.UseAuthentication();
            app.UseAuthorization();

            app.UseCors(x => x.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader());

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapHub<ChatHub>("/chat");
                endpoints.MapHub<GroupChatHub>("/groupchat");
                endpoints.MapControllers();
            });
        }
    }
}
