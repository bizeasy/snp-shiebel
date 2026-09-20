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

describe('SystemParameters e2e test', () => {
  const systemParametersPageUrl = '/system-parameters';
  let username: string;
  let password: string;
  const systemParametersSample = { key: 'joy frightfully consequently' };

  let systemParameters;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/system-parameters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/system-parameters').as('postEntityRequest');
    cy.intercept('DELETE', '/api/system-parameters/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (systemParameters) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/system-parameters/${systemParameters.id}`,
      }).then(() => {
        systemParameters = undefined;
      });
    }
  });

  it('SystemParameterses menu should load SystemParameterses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('system-parameters');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SystemParameters').should('exist');
    cy.location('pathname').should('eq', systemParametersPageUrl);
  });

  describe('SystemParameters page', () => {
    it('should have translated page title', () => {
      cy.visit(systemParametersPageUrl);
      cy.getEntityHeading('SystemParameters').should('not.contain', 'snpApp.systemParameters.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(systemParametersPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SystemParameters page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${systemParametersPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('SystemParameters');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', systemParametersPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/system-parameters',
          body: systemParametersSample,
        }).then(({ body }) => {
          systemParameters = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/system-parameters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [systemParameters],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(systemParametersPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SystemParameters page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('systemParameters');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', systemParametersPageUrl);
      });

      it('edit button click should load edit SystemParameters page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SystemParameters');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', systemParametersPageUrl);
      });

      it('edit button click should load edit SystemParameters page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SystemParameters');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', systemParametersPageUrl);
      });

      it('last delete button click should delete instance of SystemParameters', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('systemParameters').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', systemParametersPageUrl);

        systemParameters = undefined;
      });
    });
  });

  describe('new SystemParameters page', () => {
    beforeEach(() => {
      cy.visit(systemParametersPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SystemParameters');
    });

    it('should create an instance of SystemParameters', () => {
      cy.get(`[data-cy="key"]`).type('wholly');
      cy.get(`[data-cy="key"]`).should('have.value', 'wholly');

      cy.get(`[data-cy="value"]`).type('sew broadly bathrobe');
      cy.get(`[data-cy="value"]`).should('have.value', 'sew broadly bathrobe');

      cy.get(`[data-cy="description"]`).type('as');
      cy.get(`[data-cy="description"]`).should('have.value', 'as');

      cy.get(`[data-cy="environment"]`).type('likewise');
      cy.get(`[data-cy="environment"]`).should('have.value', 'likewise');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        systemParameters = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', systemParametersPageUrl);
    });
  });
});
