import { Link, NavLink } from "react-router-dom";

import "./index.scoped.css";
import logo from "./logo.svg";

const links = [
  ["/", "Студенты"],
  ["/study-group", "Группы"],
];

function NavBar() {
  return (
    <nav>
      <div className="content">
        <Link to="/"><img src={logo} alt="logo" className="logo" /></Link>
        <div className="filler" />
        {links.map(([path, text], i) => 
          <NavLink key={i} to={path}
            className={({ isActive }) => isActive ? "sidelink" : "sidelink"}>{text}</NavLink>
        )}
      </div>
    </nav>
  );
}

export default NavBar;
