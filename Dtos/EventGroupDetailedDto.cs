using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Dtos
{
    public class EventGroupDetailedDto
    {
        public int id { get; set; }
        public int? IdL { get; set; }
        public int? idU { get; set; }
        public string groupName { get; set; }
        public DateTime? creationTime { get; set; }
        public DateTime? meetTime { get; set; }
        public sbyte? type { get; set; }
        public string description { get; set; }
        public sbyte? active { get; set; }
        public LocationDetailedDto location { get; set; }
    }
}
