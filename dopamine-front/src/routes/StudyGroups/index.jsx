import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

import AppContainer from "components/AppContainer";
import AppBody from "components/AppBody";
import NavBar from "components/NavBar";

import { fetchStudyGroups } from "api";

import "./index.scoped.css";

function groupBy(list, keyName) {
  const map = new Map();
  list.forEach((item) => {
    const key = item[keyName];
    const collection = map.get(key);
    if (!collection) {
      map.set(key, [item]);
    } else {
      collection.push(item);
    }
  });
  return Array.from(map, ([k, entries]) => ({ [keyName]: k, entries }));
}

function StudyGroups() {
  const [data, setData] = useState(null);

  useEffect(() => { 
    (async () => {
      let groups = await fetchStudyGroups();
      
      let data = groupBy(groups, "qualification").map(({ qualification, entries }) => ({
        qualification, entries: groupBy(entries, "grade").map(({ grade, entries }) => ({
          grade, entries: groupBy(entries, "faculty")
        }))
      }));
      
      setData(data);
    })();
  }, []);

  return (
    <AppContainer>
      <NavBar/>
      <AppBody>
        <h1>Учебные группы</h1>
        {data && data.map(({ qualification, entries }) => (
          <div key={qualification}>
            <h2>{qualification}</h2>
            { entries.map(({ grade, entries }) => (
              <div key={grade}>
                <h3>{grade} курс</h3>
                { entries.map(({ faculty, entries }) => (
                  <div key={faculty}>
                    <div className="card">
                      <h4>{faculty}</h4>
                      <div className="groups">
                        { entries.map((group, i) => (
                          <Link key={i} to={"/study-group/" + group.id}>{group.name}</Link>
                        ))}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            ))}
          </div>
        ))}
      </AppBody>
    </AppContainer>
  );
}

export default StudyGroups;
