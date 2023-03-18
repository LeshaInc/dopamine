import { useState } from "react";

import "./index.scoped.css";

import Popup from "components/Popup";

function SortSelector({ sortDir, sortField, setSortDir, setSortField }) {
  const [visible, setVisible] = useState(false);
  
  const options = new Map();
  options.set("name", "ФИО")
  options.set("group", "группе")
  options.set("grade", "курсу")
  options.set("id", "номеру")

  return (
    <div className="root">
      Сортировка по
      <Popup visible={visible} onClose={() => setVisible(false)} offsetX={-17} button={
        <div className="button" onClick={() => setVisible(true)}>{options.get(sortField)}</div>
      }>
        <ul>
          {Array.from(options.entries()).map(([ id, name ], i) => (
            <li key={i} style={{color: sortField === id ? "black" : "#666"}} onClick={() => {setSortField(id); setVisible(false)}}>{name}</li>
          ))}
        </ul>
      </Popup>
      в порядке
      <div className="button" onClick={() => setSortDir(sortDir === "asc" ? "desc" : "asc")}>
        { sortDir === "asc" ? "возрастания" : "убывания" }
      </div>
    </div>
  )
}

export default SortSelector;
