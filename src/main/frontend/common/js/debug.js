function userInfo() {
  return {
    name: "Snoopy",
    email: "snoopy@qq.com",
    phone: 4123203825,
    birth: "2002-05-03",
    work: [
      {
        "cid": 1,
        "company": "Tintri",
        "position": "Software Engineer Intern",
        "year": {
          "end": 2014,
          "start": 2014
        }
      },
      {
        "cid": 2,
        "company": "Bicasl, SJTU",
        "position": "Research Assistant",
        "year": {
          "end": 2013,
          "start": 2012
        }
      },
      {
        "cid": 3,
        "company": "Google",
        "position": "Software Engineer",
        "year": {
          "end": 2018,
          "start": 2015
        }
      }
    ],
    education: [
      {
        "major": "Electrical Engineering",
        "school": "Shanghai Jiao Tong University",
        "sid": 2,
        "year": {
          "end": 2013,
          "start": 2009
        }
      },
      {
        "major": "Computer Engineering",
        "school": "Carnegie Mellon University",
        "sid": 3,
        "year": {
          "end": 2015,
          "start": 2013
        }
      }
    ],
    places: [
      {
        "current": false,
        "hometown": true,
        "pid": 3,
        "name": "Haimen, Jiangsu Province, China"
      },
      {
        "current": true,
        "hometown": false,
        "pid": 4,
        "name": "Sunnyvale, CA"
      }
    ]
  }
}

export default {
  userInfo: userInfo,
}