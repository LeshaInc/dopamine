import { useCallback } from "react";

import { BiLeftArrowAlt, BiRightArrowAlt } from "react-icons/bi";

import "./index.scoped.css";

function Pagination(props) {
  const onPageLeft = useCallback(() => {
    props.onPageChange(Math.max(1, props.currentPage - 1));
  }, [props]);

  const onPageRight = useCallback(() => {
    props.onPageChange(Math.min(props.pageCount, props.currentPage + 1));
  }, [props]);

  let leftPages = Math.min(props.currentPage - 1, 2);
  let rightPages = Math.min(props.pageCount - props.currentPage, 2);
  leftPages = Math.min(props.currentPage - 1, 4 - rightPages);
  rightPages = Math.min(props.pageCount - props.currentPage, 4 - leftPages);
  
  return (
    <div className="pagination">
      <div onClick={onPageLeft} className="page link"><BiLeftArrowAlt/></div>
      {props.currentPage >= 4 &&
        <div onClick={() => props.onPageChange(1)} className="page link">1</div>
      }
      {props.currentPage >= 5 &&
        <div className="page">...</div>
      }
      {Array(leftPages).fill().map((_, i) => props.currentPage + i - leftPages).map((v) => (
        <div onClick={() => props.onPageChange(v)} className="page link" key={v}>{v}</div>
      ))}
      <div className="page active">{props.currentPage}</div>
      {Array(rightPages).fill().map((_, i) => props.currentPage + i + 1).map((v) => (
        <div onClick={() => props.onPageChange(v)} className="page link" key={v}>{v}</div>
      ))}
      {props.currentPage <= props.pageCount - 4 && (
        <div className="page">...</div>
      )}
      {props.currentPage <= props.pageCount - 3 && (
        <div onClick={() => props.onPageChange(props.pageCount)} className="page link">{props.pageCount}</div>
      )}
      <div onClick={onPageRight} className="page link"><BiRightArrowAlt/></div>
    </div>
  );
}

export default Pagination;
