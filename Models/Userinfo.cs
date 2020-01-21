using System;
using System.Collections.Generic;

namespace MapChatServer.Models
{
    public partial class Userinfo
    {
        public Userinfo()
        {
            Eventgroupuser = new HashSet<Eventgroupuser>();
            FriendFriendNavigation = new HashSet<Friend>();
            FriendUser = new HashSet<Friend>();
            Messageeg = new HashSet<Messageeg>();
            MessageuReceiver = new HashSet<Messageu>();
            MessageuSender = new HashSet<Messageu>();
        }

        public int Id { get; set; }
        public string FacebookId { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string ProfilePhotoUrl { get; set; }
        public DateTime? LastOnline { get; set; } = DateTime.Now;

        public virtual ICollection<Eventgroupuser> Eventgroupuser { get; set; }
        public virtual ICollection<Friend> FriendFriendNavigation { get; set; }
        public virtual ICollection<Friend> FriendUser { get; set; }
        public virtual ICollection<Messageeg> Messageeg { get; set; }
        public virtual ICollection<Messageu> MessageuReceiver { get; set; }
        public virtual ICollection<Messageu> MessageuSender { get; set; }
    }
}
