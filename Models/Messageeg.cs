using System;

namespace MapChatServer.Models
{
    public partial class Messageeg
    {
        public int Id { get; set; }
        public int? IdU { get; set; }
        public int? IdEg { get; set; }
        public string MessageText { get; set; }
        public DateTime? CTime { get; set; } = DateTime.Now;

        public virtual Eventgroup IdEgNavigation { get; set; }
        public virtual Userinfo IdUNavigation { get; set; }
    }
}
