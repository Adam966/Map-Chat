using System;
using System.Collections.Generic;

namespace MapChatServer.Models
{
    public partial class Eventgroup
    {
        public Eventgroup()
        {
            Eventgroupuser = new HashSet<Eventgroupuser>();
            Eventtag = new HashSet<Eventtag>();
            Messageeg = new HashSet<Messageeg>();
        }

        public int Id { get; set; }
        public int? IdL { get; set; }
        public int? idU { get; set; }
        public string GroupName { get; set; }
        public DateTime? CreationTime { get; set; }
        public DateTime? MeetTime { get; set; }
        public sbyte? Type { get; set; }
        public string Description { get; set; }
        public sbyte? active { get; set; } = 1;

        public virtual Location IdLNavigation { get; set; }
        public virtual ICollection<Eventgroupuser> Eventgroupuser { get; set; }
        public virtual ICollection<Eventtag> Eventtag { get; set; }
        public virtual ICollection<Messageeg> Messageeg { get; set; }
    }
}
