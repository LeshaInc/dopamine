import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, redirect } from "react-router-dom";

import "./index.css";

import Students from "routes/Students";
import Student from "routes/Student";
import StudyGroups from "routes/StudyGroups";
import StudyGroup from "routes/StudyGroup";
import StudyStream from "routes/StudyStream";
import Error from "routes/Error";
import NotFound from "routes/NotFound";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Students/>,
    errorElement: <Error/>,
  },
  {
    path: "/student",
    element: <Students/>,
    errorElement: <Error/>,
  },
  {
    path: "/student/:id",
    element: <Student/>,
    errorElement: <Error/>,
  },
  {
    path: "/study-group",
    element: <StudyGroups/>,
    errorElement: <Error/>,
  },
  {
    path: "/study-group/:id",
    element: <StudyGroup/>,
    errorElement: <Error/>,
  },
  {
    path: "/study-stream/:id",
    element: <StudyStream/>,
    errorElement: <Error/>,
  },
  {
    path: "/*",
    element: <NotFound/>,
  }
], {
  basename: process.env.PUBLIC_URL
});

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);