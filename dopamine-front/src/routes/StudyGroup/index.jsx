import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

import AppContainer from "components/AppContainer";
import AppBody from "components/AppBody";
import NavBar from "components/NavBar";
import Schedule from "components/Schedule";

import { fetchStudyGroup } from "api";

import "./index.scoped.css";

function StudyGroup() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [data, setData] = useState(null);

  useEffect(() => { 
    (async () => {
      let data = await fetchStudyGroup(id);
      data.students.sort((a, b) => a.fullName.localeCompare(b.fullName, "ru"));
      setData(data);
    })();
  }, [id]);

  return (
    <AppContainer>
      <NavBar/>
      <AppBody>
        <h1>Учебные группы{data && " — " + data.name}</h1>
        {data && <>
          <h2>{data.facultyShort} / {data.qualification} / {data.grade} курс</h2>
        </>}
        {data && <table class="students">
          <thead>
            <th>Номер</th>
            <th>ФИО</th>
          </thead>
          <tbody>
            {data.students.map((student, i) => (
              <tr key={i} onClick={() => navigate("/student/" + student.id)}>
                <td>{student.id}</td>
                <td>{student.fullName}</td>
              </tr>
            ))}
          </tbody>
        </table>}
        {data && <div>
          <h1 class="aboba">Расписание группы</h1>
          <Schedule id={ data.scheduleId }/>
        </div>}
      </AppBody>
    </AppContainer>
  );
}

export default StudyGroup;
