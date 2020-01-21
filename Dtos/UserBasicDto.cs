using System;

namespace MapChatServer.Dtos
{
    public class UserBasicDto
    {
        public string FacebookId { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string ProfilePhotoUrl { get; set; }
        public DateTime? LastOnline { get; set; }
    }
}
