const API_ROOT = "/api";

async function get(url, query = {}) {
  let res = await fetch(API_ROOT + url + "?" + new URLSearchParams(query));
  return await res.json();
}

export async function fetchStudents(page, query = {}) {
  return get("/student", { page, ...query })
}

export async function fetchStudent(id) {
  return get("/student/" + id)
}

export async function fetchSchedule(id, start, end) {
  return get("/schedule/" + id, { start: Math.floor(start.valueOf() / 1000), end: Math.floor(end.valueOf() / 1000) })
}

export async function fetchStudyGroups() {
  return get("/study-group")
}

export async function fetchStudyGroup(id) {
  return get("/study-group/" + id)
}

export async function fetchStudyStream(id) {
  return get("/study-stream/" + id)
}

export async function autocomplete(resource, field, value) {
  return get("/" + resource + "/autocomplete/" + field, { value })
}
