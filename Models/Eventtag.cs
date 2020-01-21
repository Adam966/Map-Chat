
namespace MapChatServer.Models
{
    public partial class Eventtag
    {
        public int Id { get; set; }
        public int? IdEg { get; set; }
        public int? IdT { get; set; }

        public virtual Eventgroup IdEgNavigation { get; set; }
        public virtual Tag IdTNavigation { get; set; }
    }
}
