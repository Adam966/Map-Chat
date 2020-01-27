using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Dtos
{
    public class MessageGroupDto
    {
        public int? IdU { get; set; }
        public int? IdEg { get; set; }
        public string MessageText { get; set; }
        public DateTime? CTime { get; set; } = DateTime.Now;
    }
}
