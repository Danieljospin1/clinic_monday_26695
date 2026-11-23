import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'
import Office from './newOffice'
import Offices from './offices'
import Home from './home'
import { BrowserRouter, Routes, Route } from "react-router-dom";

function HandleClick(props) {
  return (<p>{props.words}</p>)
}

function App() {
  const [words, setWords] = useState("");
  const [showWords, setShowWords] = useState(false);

  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />} />

          <Route path="/create-office" element={<Office />} />

          <Route path="/all-offices" element={<Offices />} />
        </Routes>
      </BrowserRouter>


    </>
  )
}



export default App
