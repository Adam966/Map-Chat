
namespace MapChatServer.Models
{
    public partial class Friend
    {
        public int Id { get; set; }
        public int? UserId { get; set; }
        public int? FriendId { get; set; }
        public sbyte? Active { get; set; } = 1;

        public virtual Userinfo FriendNavigation { get; set; }
        public virtual Userinfo User { get; set; }
    }
}
