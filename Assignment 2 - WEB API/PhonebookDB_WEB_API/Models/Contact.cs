using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PhonebookDB_WEB_API.Models
{
    public class Contact
    {
        public int id { get; set; }

        //public int localID { get; set; }
        public string name { get; set; }
        public string phone { get; set; }
        public string email { get; set; }
        public string date { get; set; }
    }
}
