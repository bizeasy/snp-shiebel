/* eslint-disable @typescript-eslint/ban-ts-comment */
// @ts-nocheck
const angularLanguages = {
  en: async (): Promise<void> => import('@angular/common/locales/en'),
  hi: async (): Promise<void> => import('@angular/common/locales/hi'),
  te: async (): Promise<void> => import('@angular/common/locales/te'),
  // jhipster-needle-i18n-language-angular-loader - JHipster will add languages in this object
};

const languagesData = {
  en: async (): Promise<any> => import('i18n/en.json').catch(),
  hi: async (): Promise<any> => import('i18n/hi.json').catch(),
  te: async (): Promise<any> => import('i18n/te.json').catch(),
  // jhipster-needle-i18n-language-loader - JHipster will add languages in this object
};

export const loadLocale = (locale: keyof typeof angularLanguages): Promise<any> => {
  angularLanguages[locale]();
  return languagesData[locale]();
};
