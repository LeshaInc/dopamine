import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

import AppContainer from "components/AppContainer";
import AppBody from "components/AppBody";
import NavBar from "components/NavBar";
import Schedule from "components/Schedule";

import { fetchStudyStream } from "api";

import "./index.scoped.css";

function StudyStream() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [data, setData] = useState(null);

  useEffect(() => { 
    (async () => {
      let data = await fetchStudyStream(id);
      data.students.sort((a, b) => a.fullName.localeCompare(b.fullName, "ru"));
      setData(data);
    })();
  }, [id]);

  return (
    <AppContainer>
      <NavBar/>
      <AppBody>
        <h1>Учебные потоки{data && " — " + data.name}</h1>
        {data && <table class="students">
          <thead>
            <th>Номер</th>
            <th>ФИО</th>
            <th>Группа</th>
          </thead>
          <tbody>
            {data.students.map((student, i) => (
              <tr key={i} onClick={() => navigate("/student/" + student.id)}>
                <td>{student.id}</td>
                <td>{student.fullName}</td>
                <td>{student.studyGroup.name}</td>
              </tr>
            ))}
          </tbody>
        </table>}
        {data && <div>
          <h1 class="aboba">Расписание потока</h1>
          <Schedule id={ data.scheduleId }/>
        </div>}
      </AppBody>
    </AppContainer>
  );
}

export default StudyStream;
