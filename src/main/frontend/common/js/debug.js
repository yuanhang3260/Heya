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

function posts() {
  return [
    {
      pid: "abc123",
      username: "snoopy",
      time: new Date("April 21, 2018 15:45:00"),
      avatar: "dist/img/profile.jpg",
      content: "Hi I'm Snoopy - smart dog, cute dog.",
      images: [
        {
          url: "dist/post-images/snoopy1.png"
        },
        {
          url: "dist/post-images/snoopy2.jpg"
        },
        {
          url: "dist/post-images/snoopy3.jpg"
        },
        {
          url: "dist/post-images/snoopy4.jpg"
        },
        {
          url: "dist/post-images/snoopy5.png"
        },
      ],
    },

    {
      pid: "xyz07",
      username: "snoopy",
      time: new Date("April 20, 2018 10:20:00"),
      avatar: "dist/img/profile.jpg",
      content: "Wow ~",
      images: [
        {
          url: "dist/post-images/beauty1.jpg"
        },
        {
          url: "dist/post-images/beauty2.jpg"
        },
        {
          url: "dist/post-images/beauty3.jpg"
        },
        {
          url: "dist/post-images/beauty4.jpg"
        },
        {
          url: "dist/post-images/beauty5.jpg"
        },
        {
          url: "dist/post-images/beauty6.jpg"
        },
        {
          url: "dist/post-images/beauty7.jpg"
        },
        {
          url: "dist/post-images/beauty8.jpg"
        },
      ],
    },

    {
      pid: "mno135",
      username: "snoopy",
      time: new Date("April 17, 2018 12:35:00"),
      avatar: "dist/img/profile.jpg",
      content: "This is my buddy big bear ~",
      images: [
        {
          url: "dist/post-images/panda.jpg"
        },
      ],
    },

    {
      pid: "tyu_789",
      username: "snoopy",
      time: new Date("April 12, 2018 11:40:00"),
      avatar: "dist/img/profile.jpg",
      content: "Sleeping ~~~",
    },
  ];
}

function friends() {
  return [
    {
      username: "panda",
      avatar: "dist/img/panda.jpg",
      online: true,
    },
    {
      username: "hyuan",
      avatar: "dist/img/hyuan.jpg",
      online: false,
    }
  ];
}

function dialogs() {
  return [
    {
      minimized: false,
      friend: {
        username: "panda",
        avatar: "dist/img/panda.jpg",
        online: true,
      },
      messages: [
        {
          me: false,
          content: "hello",
          timestamp: new Date(),
        },
        {
          me: false,
          content: "...",
          timestamp: new Date(),
        },
        {
          me: true,
          content: "Hi benxiong",
          timestamp: new Date(),
        },
        {
          me: false,
          content: "oooh",
          timestamp: new Date(),
        },
      ],
    }
  ];
}

export default {
  userInfo: userInfo,
  posts: posts,
  friends: friends,
  dialogs: dialogs,
}
