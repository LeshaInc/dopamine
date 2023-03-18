import { useState, useEffect } from "react";
import { DateTime } from "luxon";

import { BiTime, BiMap, BiUser, BiLeftArrowAlt, BiRightArrowAlt } from "react-icons/bi";

import { fetchSchedule } from "api";

import "./index.scoped.css";

function toPixels(diff) {
  return diff.minutes * 1.5;
}

function formatDate(date) {
  let str = date.setLocale("ru")
    .toLocaleString({month: "long", day: "numeric"});
  return str.charAt(0).toUpperCase() + str.slice(1);
}

function formatWeekDay(date) {
  let str = date.setLocale("ru")
    .toLocaleString({weekday: "short", month: "short", day: "numeric"});
  return str.charAt(0).toUpperCase() + str.slice(1);
}

function Schedule({ id }) {
  const [data, setData] = useState(null);
  const [weekOffset, setWeekOffset] = useState(0);

  const baseDate = DateTime.now().setZone("Europe/Moscow").startOf("week").plus({weeks: weekOffset})
  
  useEffect(() => {
    (async () => {
      let data = await fetchSchedule(id, baseDate, baseDate.plus({days: 6}));
  
      for (let entry of data) {
        entry.timeStart = DateTime.fromSQL(entry.timeStart);
        entry.timeEnd = DateTime.fromSQL(entry.timeEnd);
      }

      setData(data);
    })();
  }, [weekOffset, id]);
  
  // let [minTime, maxTime] = [baseDate, baseDate];
  // let markers = [];
  
  // if (data) {
  //   let set = { day: baseDate.day, month: baseDate.month, year: baseDate.year };
  //   minTime = DateTime.min(...data.map(d => d.timeStart.set(set))) || minTime;
  //   maxTime = DateTime.max(...data.map(d => d.timeEnd.set(set))) || maxTime;
  //   markers = [...new Set(data.map(v => v.timeStart.set(set).toISO()))];
  //   markers = markers.map(v => DateTime.fromISO(v));
  // }
  
  let minTime = baseDate.plus({hours: 8, minutes: 20});
  let maxTime = baseDate.plus({hours: 21, minutes: 50});
  let markers = [[8, 20], [10, 0], [11, 40], [13, 30], [15, 20], [17, 0], [18, 40], [20, 20]]
    .map(([hours, minutes]) => baseDate.plus({hours, minutes}));
  
  let height = Math.max(toPixels(maxTime.diff(minTime, "minutes")) + 50, 200);
  
  return (data &&
    <div className="card">
      <div className="controls">
        <div className="button" onClick={() => setWeekOffset(0)}><BiTime/> Текущая неделя</div>
        <div className="button" onClick={() => setWeekOffset(weekOffset - 1)}><BiLeftArrowAlt/></div>
        <div className="range">
         {formatDate(baseDate)} – {formatDate(baseDate.plus({days:5}))}
        </div>
        <div className="button" onClick={() => setWeekOffset(weekOffset + 1)}><BiRightArrowAlt/></div>
      </div>
      <div className="cols">
        <div className="time-col">
          {markers.map((time, i) => {
            let top = toPixels(time.diff(minTime, "minutes")) + 49;
            return [
              <div key={2*i} className="time-label" style={{top}}>{time.toFormat("HH:mm")}</div>,
              <div key={2*i+1} className="time-line" style={{top}}></div>
            ];
          })}
        </div>
        {Array(6).fill().map((_, i) => (
          <Column data={data} baseDate={baseDate} minTime={minTime} index={i} height={height} key={i}>
            {markers.map((time, i) => {
              let top = toPixels(time.diff(minTime, "minutes")) + 49;
              return (
                <div key={i} className="time-line" style={{top}}></div>
              );
            })}
          </Column>
        ))}
      </div>
    </div>
  );
}

function Column({ children, data, baseDate, minTime, index, height }) {
  const date = baseDate.set({weekday: index + 1});
  minTime = minTime.set({weekday: index + 1});
  
  const items = data.filter(v => v.timeStart.hasSame(date, "day"));
  
  const colors = {
    "Лекции": "#0091ff",
    "Лабораторные занятия": "#a50aff",
    "Консультация": "#1846c7",
    "Практические занятия": "#f7b500",
  };
  
  return (
    <div className="col" style={{ height }}>
      <div className="header">
        {formatWeekDay(date)}
      </div>
      {children}
      {items.map((entry, j) => {
        let beforeOverlap = j > 0 && items[j - 1].timeStart.equals(entry.timeStart);
        let afterOverlap = j < items.length - 1 && items[j + 1].timeStart.equals(entry.timeStart);
        let overlap = beforeOverlap || afterOverlap;
        
        let top = toPixels(entry.timeStart.diff(minTime, "minutes")) + 50;
        let bottom = toPixels(entry.timeEnd.diff(minTime, "minutes")) + 50;
        let height = bottom - top;
        let left = (beforeOverlap ? 50 : 0) + "%";
        let width = (overlap ? 50 : 100)+ "%";

        let subject = entry.subjectName;
        if (overlap) subject = subject.replace(/(.)/g, "$1​");

        let teacher = entry.teacher;
        if (overlap) teacher = teacher.replace(/(.)/g, "$1​");

        let color = colors[entry.subjectKind];
        
        return <div className="item" key={j} style={{ left, top, width, height, borderColor: color }}>
          <div className="time" style={{ color }}>
            {entry.timeStart.toFormat("HH:mm")}–{entry.timeEnd.toFormat("HH:mm")}
          </div>
          <div className="subj">{subject}</div>
          {entry.place.length > 0 &&
            <div><BiMap/>{entry.place.replace("\n", ", ")}</div>}
          {entry.teacher.length > 0 && entry.teacher !== "-" &&
            <div><BiUser/>{teacher}</div>}
        </div>;
      })}
    </div>
  );
}

export default Schedule;
