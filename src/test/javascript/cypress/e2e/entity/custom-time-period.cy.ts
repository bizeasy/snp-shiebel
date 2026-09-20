import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('CustomTimePeriod e2e test', () => {
  const customTimePeriodPageUrl = '/custom-time-period';
  let username: string;
  let password: string;
  const customTimePeriodSample = {};

  let customTimePeriod;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/custom-time-periods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/custom-time-periods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/custom-time-periods/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (customTimePeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/custom-time-periods/${customTimePeriod.id}`,
      }).then(() => {
        customTimePeriod = undefined;
      });
    }
  });

  it('CustomTimePeriods menu should load CustomTimePeriods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('custom-time-period');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustomTimePeriod').should('exist');
    cy.location('pathname').should('eq', customTimePeriodPageUrl);
  });

  describe('CustomTimePeriod page', () => {
    it('should have translated page title', () => {
      cy.visit(customTimePeriodPageUrl);
      cy.getEntityHeading('CustomTimePeriod').should('not.contain', 'snpApp.customTimePeriod.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(customTimePeriodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustomTimePeriod page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${customTimePeriodPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustomTimePeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customTimePeriodPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/custom-time-periods',
          body: customTimePeriodSample,
        }).then(({ body }) => {
          customTimePeriod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/custom-time-periods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [customTimePeriod],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(customTimePeriodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustomTimePeriod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('customTimePeriod');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customTimePeriodPageUrl);
      });

      it('edit button click should load edit CustomTimePeriod page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomTimePeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customTimePeriodPageUrl);
      });

      it('edit button click should load edit CustomTimePeriod page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomTimePeriod');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customTimePeriodPageUrl);
      });

      it('last delete button click should delete instance of CustomTimePeriod', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('customTimePeriod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customTimePeriodPageUrl);

        customTimePeriod = undefined;
      });
    });
  });

  describe('new CustomTimePeriod page', () => {
    beforeEach(() => {
      cy.visit(customTimePeriodPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustomTimePeriod');
    });

    it('should create an instance of CustomTimePeriod', () => {
      cy.get(`[data-cy="name"]`).type('freezing yippee');
      cy.get(`[data-cy="name"]`).should('have.value', 'freezing yippee');

      cy.get(`[data-cy="fromDate"]`).type('2023-12-14');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        customTimePeriod = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', customTimePeriodPageUrl);
    });
  });
});
