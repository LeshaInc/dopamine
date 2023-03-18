import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";

import { BiGroup } from "react-icons/bi";

import AppContainer from "components/AppContainer";
import AppBody from "components/AppBody";
import NavBar from "components/NavBar";
import Schedule from "components/Schedule";

import { fetchStudent } from "api";

import "./index.scoped.css";

function Student() {
  let { id } = useParams();
  const [data, setData] = useState(null);

  useEffect(() => {
    (async (page) => {
      let data = await fetchStudent(id);
      data.studyStreams.sort((a, b) => a.name.localeCompare(b.name));
      setData(data);
    })();
  }, [id]);
  
  return (
    <AppContainer>
      <NavBar/>
      {data && <AppBody>
        <h1>Студенты ФПИиКТ</h1>
        <div className="card">
          <div className="card-bg"></div>
          <div className="avatar"></div>
          <div className="card-body">
            <div className="card-right">
              <h2 className="name">{data.fullName}</h2>
              <Link to={"/study-group/" + data.studyGroup.id} className="group"><BiGroup/>{data.studyGroup.name}</Link>
            </div>
            <div className="card-details">
              <div className="info">
                <h3>{data.studyGroup.facultyShort} / {data.studyGroup.qualification} / {data.studyGroup.grade} курс</h3>
              </div>
              {data.studyStreams.length > 0 && <div className="streams">
                <h3>Учебные потоки:</h3>
                {data.studyStreams.map((stream, i) => (
                  <Link to={"/study-stream/" + stream.id} key={i}>{stream.name}</Link>
                ))}
              </div>}
            </div>
          </div>
        </div>
        <h1>Персональное расписание</h1>
        <Schedule id={"student/" + id}/>
      </AppBody>}
    </AppContainer>
  );
}

export default Student;
