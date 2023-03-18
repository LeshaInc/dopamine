import { useState, useRef, useEffect } from "react";
import { CSSTransition } from "react-transition-group";

import "./index.scoped.css";

function Popup({ visible, onClose, button, children, offsetX }) {
  const [calculatedOffsetX, setCalculatedOffsetX] = useState(0);
  const nodeRef = useRef(null);
  const containerRef = useRef(null);
  
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (visible && nodeRef !== null && nodeRef.current && !nodeRef.current.contains(event.target)) {
        onClose();
      }
    }

    document.addEventListener("mouseup", handleClickOutside);
    return () => {
      document.removeEventListener("mouseup", handleClickOutside);
    };
  }, [nodeRef, visible, onClose]);
  
  useEffect(() => {
    if (!nodeRef.current || !containerRef.current || !visible) return;
    let posX = containerRef.current.getBoundingClientRect().left;
    let width = nodeRef.current.getBoundingClientRect().width;
    let offset = Math.max(posX + width - document.documentElement.clientWidth + 16, 0);
    setCalculatedOffsetX(Math.floor(offsetX - offset));
  }, [nodeRef, containerRef, offsetX, visible]);
  
  return (
    <div className="container" ref={containerRef}>
      {button}
      <CSSTransition nodeRef={nodeRef} in={visible} timeout={100} unmountOnExit classNames="popup">
        <div className="popup" style={{left: calculatedOffsetX}} ref={nodeRef}>
          {children}
        </div>
      </CSSTransition>
    </div>
  )
}

export default Popup;
