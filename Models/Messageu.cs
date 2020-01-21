using System;

namespace MapChatServer.Models
{
    public partial class Messageu
    {
        public int Id { get; set; }
        public int? SenderId { get; set; }
        public int? ReceiverId { get; set; }
        public string MessageText { get; set; }
        public DateTime? CTime { get; set; } = DateTime.Now;

        public virtual Userinfo Receiver { get; set; }
        public virtual Userinfo Sender { get; set; }
    }
}
