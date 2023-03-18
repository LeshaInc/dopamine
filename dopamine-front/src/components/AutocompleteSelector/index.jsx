import { useEffect, useState, useRef, useCallback } from "react";
import { BiSearch, BiChevronDown } from "react-icons/bi";
import { DebounceInput } from 'react-debounce-input';

import Popup from "components/Popup";
import { autocomplete } from "api";

import "./index.scoped.css";

function AutocompleteSelector({ resource, field, fieldName, width, onSelect }) {
  const [visible, setVisible] = useState(false);
  const [value, setValue] = useState("");
  const [data, setData] = useState(null);
  const inputRef = useRef(null);
  
  const open = useCallback(() => {
    setVisible(true);
    setTimeout(() => inputRef.current.focus(), 50);
  }, []);
  
  const select = useCallback(v => {
    onSelect(v);
    setVisible(false);
    setValue("");
  }, [onSelect]);
  
  const onKeyPress = useCallback(e => {
    if (e.key === "Enter") {
      let value = e.target === inputRef.current ? data[0] : e.target.innerHTML;
      if (value) {
        select(value);
        return;
      }
    }

    if (e.target !== inputRef.current) {
      let char = String.fromCharCode(e.keyCode || e.which);
      setValue(value + char);
      inputRef.current.focus();
    }
  }, [data, select, value]);
  
  const onKeyDown = useCallback(e => {
    if (e.key === "Backspace") {
      setValue(value.slice(0, value.length - 1));
      inputRef.current.focus();
    }
  }, [value])
  
  useEffect(() => {
    (async () => {
      if (!visible) return;
      let data = await autocomplete(resource, field, value);
      setData(data);
    })();
  }, [visible, value, resource, field]);
  
  return (
    <Popup visible={visible} onClose={() => setVisible(false)} offsetX={-17} button={
      <div className="button" onClick={open}>{fieldName}<BiChevronDown/></div>
    }>
      <div className="input">
        <DebounceInput inputRef={inputRef} debounceTimeout={200} value={value}
          onChange={e => setValue(e.target.value)} onKeyPress={onKeyPress} placeholder="Поиск" />
        <BiSearch/>
      </div>
      <ul style={{width: (width || 300)+"px"}} tabIndex="-1">
        {data && data.map((v, i) => (
          <li tabIndex={i === 0 ? -1 : 0} key={i} onClick={() => select(v)} onKeyPress={onKeyPress} onKeyDown={onKeyDown}>{v}</li>
        ))}
      </ul>
    </Popup>
  );
}

export default AutocompleteSelector;
