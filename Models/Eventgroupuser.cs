
namespace MapChatServer.Models
{
    public partial class Eventgroupuser
    {
        public int Id { get; set; }
        public int? IdEg { get; set; }
        public int? IdU { get; set; }
        public sbyte? Admin { get; set; }
        public sbyte? Active { get; set; } = 1;

        public virtual Eventgroup IdEgNavigation { get; set; }
        public virtual Userinfo IdUNavigation { get; set; }
    }
}
