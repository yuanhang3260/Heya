function profileImageURL(username) {
  if (!this.debug) {
    return "profileimage/profile/" + (username || this.username);
  } else {
    return "dist/img/profile.jpg";
  }
}

function profileCoverImageURL(username) {
  if (!this.debug) {
    return "profileimage/cover/" + (username || this.username);
  } else {
    return "dist/img/cover.jpg";
  }
}

function postImageURL(username, pid, imageFile) {
  return "post/" + username + "/" + pid + "/image/" + imageFile;
}

function generatePost(post, username) {
  post["avatar"] = "profileimage/profile/" + username;
  post["username"] = username;
  post["time"] = new Date(post.time);

  let images = [];
  for (let pic of post.pictures) {
    images.push({url: postImageURL(username, post.pid, pic)});
  }
  post["images"] = images;
  delete post["pictures"];

  return post;
}

export default {
  generatePost: generatePost,
  profileImageURL: profileImageURL,
  profileCoverImageURL: profileCoverImageURL,
}
