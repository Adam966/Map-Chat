using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Dtos
{
    public class MessageUserDto
    {
        public int? SenderId { get; set; }
        public int? ReceiverId { get; set; }
        public string MessageText { get; set; }
        public DateTime? CTime { get; set; }
    }
}
