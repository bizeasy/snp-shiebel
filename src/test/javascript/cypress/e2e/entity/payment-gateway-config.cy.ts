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

describe('PaymentGatewayConfig e2e test', () => {
  const paymentGatewayConfigPageUrl = '/payment-gateway-config';
  let username: string;
  let password: string;
  const paymentGatewayConfigSample = { name: 'psst' };

  let paymentGatewayConfig;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-gateway-configs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-gateway-configs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-gateway-configs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentGatewayConfig) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-gateway-configs/${paymentGatewayConfig.id}`,
      }).then(() => {
        paymentGatewayConfig = undefined;
      });
    }
  });

  it('PaymentGatewayConfigs menu should load PaymentGatewayConfigs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-gateway-config');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentGatewayConfig').should('exist');
    cy.location('pathname').should('eq', paymentGatewayConfigPageUrl);
  });

  describe('PaymentGatewayConfig page', () => {
    it('should have translated page title', () => {
      cy.visit(paymentGatewayConfigPageUrl);
      cy.getEntityHeading('PaymentGatewayConfig').should('not.contain', 'snpApp.paymentGatewayConfig.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentGatewayConfigPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentGatewayConfig page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${paymentGatewayConfigPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PaymentGatewayConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayConfigPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-gateway-configs',
          body: paymentGatewayConfigSample,
        }).then(({ body }) => {
          paymentGatewayConfig = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-gateway-configs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [paymentGatewayConfig],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentGatewayConfigPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentGatewayConfig page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentGatewayConfig');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayConfigPageUrl);
      });

      it('edit button click should load edit PaymentGatewayConfig page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentGatewayConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayConfigPageUrl);
      });

      it('edit button click should load edit PaymentGatewayConfig page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentGatewayConfig');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayConfigPageUrl);
      });

      it('last delete button click should delete instance of PaymentGatewayConfig', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentGatewayConfig').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayConfigPageUrl);

        paymentGatewayConfig = undefined;
      });
    });
  });

  describe('new PaymentGatewayConfig page', () => {
    beforeEach(() => {
      cy.visit(paymentGatewayConfigPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentGatewayConfig');
    });

    it('should create an instance of PaymentGatewayConfig', () => {
      cy.get(`[data-cy="name"]`).type('pfft atop yet');
      cy.get(`[data-cy="name"]`).should('have.value', 'pfft atop yet');

      cy.get(`[data-cy="authUrl"]`).type('pish rotten');
      cy.get(`[data-cy="authUrl"]`).should('have.value', 'pish rotten');

      cy.get(`[data-cy="releaseUrl"]`).type('headline brr ferret');
      cy.get(`[data-cy="releaseUrl"]`).should('have.value', 'headline brr ferret');

      cy.get(`[data-cy="refundUrl"]`).type('decriminalize');
      cy.get(`[data-cy="refundUrl"]`).should('have.value', 'decriminalize');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        paymentGatewayConfig = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', paymentGatewayConfigPageUrl);
    });
  });
});
