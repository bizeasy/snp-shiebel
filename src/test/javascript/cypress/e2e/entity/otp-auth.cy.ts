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

describe('OtpAuth e2e test', () => {
  const otpAuthPageUrl = '/otp-auth';
  let username: string;
  let password: string;
  const otpAuthSample = {};

  let otpAuth;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/otp-auths+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/otp-auths').as('postEntityRequest');
    cy.intercept('DELETE', '/api/otp-auths/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (otpAuth) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/otp-auths/${otpAuth.id}`,
      }).then(() => {
        otpAuth = undefined;
      });
    }
  });

  it('OtpAuths menu should load OtpAuths page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('otp-auth');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OtpAuth').should('exist');
    cy.location('pathname').should('eq', otpAuthPageUrl);
  });

  describe('OtpAuth page', () => {
    it('should have translated page title', () => {
      cy.visit(otpAuthPageUrl);
      cy.getEntityHeading('OtpAuth').should('not.contain', 'snpApp.otpAuth.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(otpAuthPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OtpAuth page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${otpAuthPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OtpAuth');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', otpAuthPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/otp-auths',
          body: otpAuthSample,
        }).then(({ body }) => {
          otpAuth = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/otp-auths+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/otp-auths?page=0&size=20>; rel="last",<http://localhost/api/otp-auths?page=0&size=20>; rel="first"',
              },
              body: [otpAuth],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(otpAuthPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OtpAuth page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('otpAuth');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', otpAuthPageUrl);
      });

      it('edit button click should load edit OtpAuth page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OtpAuth');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', otpAuthPageUrl);
      });

      it('edit button click should load edit OtpAuth page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OtpAuth');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', otpAuthPageUrl);
      });

      it('last delete button click should delete instance of OtpAuth', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('otpAuth').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', otpAuthPageUrl);

        otpAuth = undefined;
      });
    });
  });

  describe('new OtpAuth page', () => {
    beforeEach(() => {
      cy.visit(otpAuthPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OtpAuth');
    });

    it('should create an instance of OtpAuth', () => {
      cy.get(`[data-cy="otp"]`).type('airline in');
      cy.get(`[data-cy="otp"]`).should('have.value', 'airline in');

      cy.get(`[data-cy="otpSent"]`).should('not.be.checked');
      cy.get(`[data-cy="otpSent"]`).click();
      cy.get(`[data-cy="otpSent"]`).should('be.checked');

      cy.get(`[data-cy="verificationSuccess"]`).should('not.be.checked');
      cy.get(`[data-cy="verificationSuccess"]`).click();
      cy.get(`[data-cy="verificationSuccess"]`).should('be.checked');

      cy.get(`[data-cy="otpExpired"]`).should('not.be.checked');
      cy.get(`[data-cy="otpExpired"]`).click();
      cy.get(`[data-cy="otpExpired"]`).should('be.checked');

      cy.get(`[data-cy="otpSentTime"]`).type('2023-12-14T00:39');
      cy.get(`[data-cy="otpSentTime"]`).blur();
      cy.get(`[data-cy="otpSentTime"]`).should('have.value', '2023-12-14T00:39');

      cy.get(`[data-cy="sentCounter"]`).type('8095');
      cy.get(`[data-cy="sentCounter"]`).should('have.value', '8095');

      cy.get(`[data-cy="failCounter"]`).type('3551');
      cy.get(`[data-cy="failCounter"]`).should('have.value', '3551');

      cy.get(`[data-cy="otpResetCounter"]`).type('11995');
      cy.get(`[data-cy="otpResetCounter"]`).should('have.value', '11995');

      cy.get(`[data-cy="maxResend"]`).type('22641');
      cy.get(`[data-cy="maxResend"]`).should('have.value', '22641');

      cy.get(`[data-cy="maxReset"]`).type('14897');
      cy.get(`[data-cy="maxReset"]`).should('have.value', '14897');

      cy.get(`[data-cy="maxFailures"]`).type('32753');
      cy.get(`[data-cy="maxFailures"]`).should('have.value', '32753');

      cy.get(`[data-cy="otpActiveTime"]`).type('20730');
      cy.get(`[data-cy="otpActiveTime"]`).should('have.value', '20730');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        otpAuth = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', otpAuthPageUrl);
    });
  });
});
