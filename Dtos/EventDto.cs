using MapChatServer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Dtos
{
    public class EventDto
    {
        public int idU { get; set; }
        public string groupName { get; set; }
        public sbyte? type { get; set; }
        public string description { get; set; }

        public DateTime meetTime { get; set; }
        public string[]? tags { get; set; }
        public Location location {get; set;}
    }
}
