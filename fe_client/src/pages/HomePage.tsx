import React from 'react';
import { useOutletContext } from "react-router-dom";
import { AppContextType } from "../Root";
import { AboutSection } from "./about-section";
import { CareersPage } from "./careers-page";
import { CodegymHero } from "./codegym-hero";
import { ContactPage } from './contact-page';

export function HomePage() {
  const { onRegisterClickForHero, onApplicationFormClick } = useOutletContext<AppContextType>();

  return (
    <>
      <CodegymHero onRegisterClick={onRegisterClickForHero} />
      <AboutSection />
      <CareersPage onApplicationFormClick={onApplicationFormClick} />
      <ContactPage />
    </>
  );
}
