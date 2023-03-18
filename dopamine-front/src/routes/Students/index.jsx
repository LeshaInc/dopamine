import { useState, useRef, useEffect, useCallback } from "react";
import { Link } from "react-router-dom";
import { BiSearch } from "react-icons/bi";
import { DebounceInput } from 'react-debounce-input';

import AppContainer from "components/AppContainer";
import AppBody from "components/AppBody";
import NavBar from "components/NavBar";
import SortSelector from "components/SortSelector";
import AutocompleteSelector from "components/AutocompleteSelector";
import Pagination from "components/Pagination";

import { autocomplete, fetchStudents } from "api";

import creeper from "./creeper.png";
import enderman from "./enderman.png";
import "./index.scoped.css";

const functions = [
  { 
    keyword: "номер:",
    resource: "student",
    field: "id"
  },
  { 
    keyword: "фио:",
    resource: "student",
    field: "name"
  },
  { 
    keyword: "группа:",
    resource: "study-group",
    field: "name"
  },
  { 
    keyword: "курс:",
    resource: "study-group",
    field: "grade"
  },
  { 
    keyword: "факультет:",
    resource: "study-group",
    field: "faculty"
  },
  { 
    keyword: "квалификация:",
    resource: "study-group",
    field: "qualification"
  }
];

function Students() {
  const [data, setData] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);
  const [sortField, setSortField] = useState("name");
  const [sortDir, setSortDir] = useState("asc");
  const [selectorPos, setSelectorPos] = useState(0);
  const [selectorWord, setSelectorWord] = useState("");
  const [selectorPrevWord, setSelectorPrevWord] = useState("");
  const [selectorCandidates, setSelectorCandidates] = useState([]);
  const [selectorIndex, setSelectorIndex] = useState(0);
  const [selectorVisible, setSelectorVisible] = useState(false);
  const tableRef = useRef(null);

  const [query, setQuery] = useState("");

  useEffect(() => { 
    (async () => {
      let data = await fetchStudents(currentPage - 1, { query, sortField, sortDir });
      setData(data);
      setCurrentPage(data.currentPage + 1);
      setPageCount(data.pageCount);
    })();
  }, [currentPage, query, sortField, sortDir]);
  
  const onPageChange = useCallback(page => {
    if (currentPage === page) return;
    setCurrentPage(page);
    if (tableRef) tableRef.current.scrollIntoView();
  }, [currentPage]);
  
  const addQuery = useCallback((func, word)=> {
    if (/\s/g.test(word)) word = "(" + word + ")";
    
    if (query.length === 0 || /\s+$/.test(query)) {
      setQuery(query + func + ":" + word);
    } else {
      setQuery(query + " " + func + ":" + word);
    }
  }, [query]);

  const updateInput = useCallback(e => {
    setQuery(e.target.value);
  }, []);

  const updateSelector = useCallback(e => {
    let words = e.target.value.slice(0, e.target.selectionStart).split(/\s+/);
    let word = words.pop()
    setSelectorPos((e.target.selectionStart - word.length) * 8.395 + 23 - e.target.scrollLeft);
    setSelectorWord(word);
    setSelectorPrevWord(words.pop());
  }, []);

  const onKeyDown = useCallback(e => {
    updateSelector(e);

    if (e.key === "Tab") {
      let pos = selectorIndex + (e.shiftKey ? -1 : 1);
      if (pos < 0) { pos = selectorCandidates.length - 1 }
      setSelectorIndex(pos % selectorCandidates.length);
      e.preventDefault();
    }

    if (e.key === "Enter") {
      let pos = e.target.selectionStart;

      let word = selectorCandidates[selectorIndex];
      if (word.includes(" ")) {
        word = '(' + word + ')';
      }

      word = word + " ";

      let text = [query.slice(0, pos - selectorWord.length), word, query.slice(pos)].join('');      // setQuery(q => );
      setQuery(text);
      pos += word.length;

      window.requestAnimationFrame(() => {
        e.target.focus();
        e.target.setSelectionRange(pos, pos);
      });
    }
  }, [query, updateSelector, selectorWord, selectorIndex, selectorCandidates]);

  const onBlur = useCallback(() => {
    setSelectorVisible(false);
  }, []);

  const onFocus = useCallback(() => {
    setSelectorVisible(selectorCandidates.length > 0);
  }, [selectorCandidates]);

  useEffect(() => {
    let candidates = [];
    
    for (const fun of functions) {
      if (selectorWord.length === 0) break;
      setSelectorVisible(selectorCandidates.length > 0);
      
      if (fun.keyword === selectorPrevWord) {
        (async () => {
          let words = await autocomplete(fun.resource, fun.field, selectorWord);
          words = words.map(v => String(v));
          words = words.slice(0, 10);
          setSelectorVisible(words.length > 0);
          setSelectorCandidates(words);
          setSelectorIndex(0);
        })();

        return;
      } else if (fun.keyword.startsWith(selectorWord)) {
        candidates.push(fun.keyword)
      }
    }

    setSelectorVisible(candidates.length > 0);
    setSelectorCandidates(candidates);
    setSelectorIndex(0);
  }, [selectorPrevWord, selectorWord]);
 
  return (
    <AppContainer>
      <NavBar/>
      <AppBody >
        <h1>Студенты университета</h1>

        <label className="input selector-container">
          <BiSearch/>
          <DebounceInput debounceTimeout={200} value={query}
            onChange={updateInput} onKeyDown={onKeyDown} onSelect={updateSelector} onBlur={onBlur} onFocus={onFocus} placeholder="Поиск" />
          <div className="selector"
            style={{ left: selectorPos + "px", opacity: selectorVisible ? "1" : "0" }}>
            { selectorCandidates.map((candidate, i) => (
              <div className={i === selectorIndex ? "selected" : ""}>{ candidate }</div>
            ))}
          </div>
        </label>
        
        <div className="autocompletion">
          <AutocompleteSelector resource="student" field="id" fieldName="Номер" width={120}
            onSelect={(v) => addQuery("номер", v)}/>
          <AutocompleteSelector resource="student" field="name" fieldName="ФИО"
            onSelect={(v) => addQuery("фио", v)}/>
          <AutocompleteSelector resource="study-group" field="name" fieldName="Группа" width={120}
            onSelect={(v) => addQuery("группа", v)}/>
          <AutocompleteSelector resource="study-group" field="grade" fieldName="Курс" width={120}
            onSelect={(v) => addQuery("курс", v)}/>
          <AutocompleteSelector resource="study-group" field="faculty" fieldName="Факультет"
            onSelect={(v) => addQuery("факультет", v)}/>
          <AutocompleteSelector resource="study-group" field="qualification" fieldName="Квалификация" width={140}
            onSelect={(v) => addQuery("квалификация", v)}/>
        </div>
        
        <SortSelector {...{sortField, setSortField, sortDir, setSortDir}}/>

        <div ref={tableRef} className="container">
          <div className="cell header first-row first-col">Номер</div>
          <div className="cell header first-row">ФИО</div>
          <div className="cell header first-row last-col">Группа</div>

          {data && data.data.map(({ id, fullName, studyGroup }, i) => {
            let classes = "cell" + (i === data.data.length - 1 ? " last-row" : "");
            return [
              <Link key={i} className="row" to={"/student/" + id}>
                <div className={classes + " first-col"}>{id}</div>
                <div className={classes}>{fullName}</div>
                <Link className={classes + " last-col"} to={"/study-group/" + studyGroup.id}>{studyGroup.name}</Link>
              </Link>
            ];
          })}
          
          {data && data.data.length === 0 && (
            <div className="cell notfound last-row first-col last-col">
              <img src={creeper} alt="creeper" />
              <div>
                <h1>Ничего не найдено...</h1>
                <p>Таких студентов не существует... Либо они аспиранты и не попали в базу данных...<br/><br/>Совет: попробуйте переписать поисковый запрос.</p>
              </div>
              <img src={enderman} alt="enderman" />
            </div>
          )}
        </div>
        <Pagination currentPage={currentPage} pageCount={pageCount}
          onPageChange={onPageChange} />
      </AppBody>
    </AppContainer>
  );
}

export default Students;
